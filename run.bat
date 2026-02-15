@echo off
echo === Compilation de SREscape ===
javac SREscape.java

if %errorlevel% equ 0 (
    echo Compilation reussie !
    echo.
    echo === Lancement de SREscape ===
    echo.
    java SREscape
) else (
    echo Erreur de compilation
    pause
    exit /b 1
)
