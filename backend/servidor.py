"""Servidor de desarrollo para LexStudy. Ejecutar con Python 3."""
import csv
import json
import sqlite3
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path

BASE = Path(__file__).resolve().parent
COLECCIONES = {archivo.stem: archivo for archivo in (BASE / "xano").glob("*.csv")}


class Biblioteca(BaseHTTPRequestHandler):
    def responder(self, codigo, datos):
        contenido = json.dumps(datos, ensure_ascii=False).encode()
        self.send_response(codigo)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(contenido)))
        self.end_headers()
        self.wfile.write(contenido)

    def do_GET(self):
        archivo = COLECCIONES.get(self.path.removeprefix("/"))
        if archivo is None:
            return self.responder(404, {"error": "Ruta desconocida"})
        with archivo.open(encoding="utf-8", newline="") as fuente:
            self.responder(200, list(csv.DictReader(fuente)))

    def do_POST(self):
        if self.path != "/sugerencias":
            return self.responder(404, {"error": "Ruta desconocida"})
        try:
            largo = int(self.headers.get("Content-Length", 0))
            if not 0 < largo <= 8192:
                raise ValueError()
            datos = json.loads(self.rfile.read(largo))
            texto = datos["texto"].strip()
            if not 5 <= len(texto) <= 500:
                raise ValueError()
        except (ValueError, TypeError, KeyError, AttributeError):
            return self.responder(400, {"error": "Se requiere texto de 5 a 500 caracteres"})
        with sqlite3.connect(BASE / "sugerencias.sqlite3") as db:
            db.execute("CREATE TABLE IF NOT EXISTS sugerencias(id INTEGER PRIMARY KEY, texto TEXT NOT NULL)")
            registro = db.execute("INSERT INTO sugerencias(texto) VALUES (?)", (texto,)).lastrowid
        self.responder(201, {"id": registro})


if __name__ == "__main__":
    ThreadingHTTPServer(("127.0.0.1", 8765), Biblioteca).serve_forever()
