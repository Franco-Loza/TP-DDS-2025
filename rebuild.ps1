# Script simple para actualizar hotel-premier
# Uso: .\rebuild.ps1

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Actualizando Hotel Premier" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 1. Compilar el proyecto
Write-Host "[1/2] Compilando proyecto..." -ForegroundColor Yellow
Set-Location hotel-premier
./mvnw clean package -DskipTests
$compileResult = $LASTEXITCODE
Set-Location ..

if ($compileResult -ne 0) {
    Write-Host ""
    Write-Host "ERROR: La compilacion fallo" -ForegroundColor Red
    Write-Host "Revisa los errores arriba y corrigelos" -ForegroundColor Red
    exit 1
}

Write-Host "Compilacion exitosa!" -ForegroundColor Green
Write-Host ""

# 2. Rebuild y recrear el contenedor
Write-Host "[2/2] Reconstruyendo imagen y recreando contenedor..." -ForegroundColor Yellow
docker-compose build hotel-premier
docker-compose up -d --force-recreate --no-deps hotel-premier

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: No se pudo recrear el contenedor" -ForegroundColor Red
    Write-Host "Asegurate de que los servicios esten levantados con: docker-compose up -d" -ForegroundColor Red
    exit 1
}

Write-Host "Contenedor recreado!" -ForegroundColor Green
Write-Host ""
Write-Host "Esperando que el servicio inicie..." -ForegroundColor Gray
Start-Sleep -Seconds 5
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Aplicacion disponible en:" -ForegroundColor Green
Write-Host "  http://localhost:8080" -ForegroundColor White
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
