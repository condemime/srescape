#!/bin/bash

echo "=== Compilation de SREscape ==="
javac SREscape.java

if [ $? -eq 0 ]; then
    echo "Compilation reussie !"
    echo ""
    echo "=== Lancement de SREscape ==="
    echo ""
    java SREscape
else
    echo "Erreur de compilation"
    exit 1
fi
