#!/bin/bash

# Iniciar o servidor em segundo plano
java Server &
SERVER_PID=$!

run_client() {
    java Client "$@" &
}

# Esperar um momento para garantir que o servidor está pronto
sleep 2

run_client 10 20 30 SUM

run_client 5 10 15 SUM

run_client 2 4 6 8 10 SUM

run_client 1 2 3 MUL

run_client 3 3 3 3 SUM

sleep 1

echo "Desligando o servidor"
kill $SERVER_PID
