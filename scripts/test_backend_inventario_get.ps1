$BASE_URL = "http://localhost:8085"

Write-Host "==============================================" -ForegroundColor Cyan
Write-Host "PRUEBAS FINALES BACKEND INVENTARIO" -ForegroundColor Cyan
Write-Host "ERP_CLINICA_PRINCIPAL" -ForegroundColor Cyan
Write-Host "==============================================" -ForegroundColor Cyan

function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Url
    )

    Write-Host ""
    Write-Host "Probando: $Name" -ForegroundColor Yellow
    Write-Host $Url -ForegroundColor DarkGray

    try {
        $response = Invoke-RestMethod -Uri $Url -Method GET
        Write-Host "OK" -ForegroundColor Green
        return $response
    } catch {
        Write-Host "ERROR" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
        return $null
    }
}

$health = Test-Endpoint "Health" "$BASE_URL/api/public/health"
$catalogs = Test-Endpoint "Catalogos" "$BASE_URL/api/inventory/catalogs"
$dci = Test-Endpoint "DCI" "$BASE_URL/api/inventory/dci"
$labs = Test-Endpoint "Laboratorios" "$BASE_URL/api/inventory/laboratories"
$providers = Test-Endpoint "Proveedores" "$BASE_URL/api/inventory/providers"
$medicines = Test-Endpoint "Medicamentos" "$BASE_URL/api/inventory/medicines"
$stocks = Test-Endpoint "Inventario FEFO" "$BASE_URL/api/inventory/stocks"
$kardex = Test-Endpoint "Kardex" "$BASE_URL/api/inventory/kardex"
$dashboard = Test-Endpoint "Dashboard inventario" "$BASE_URL/api/inventory/dashboard-summary"

Write-Host ""
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host "RESUMEN RAPIDO" -ForegroundColor Cyan
Write-Host "==============================================" -ForegroundColor Cyan

if ($dashboard -ne $null) {
    Write-Host "Medicamentos activos: $($dashboard.totalMedicamentosActivos)"
    Write-Host "Lotes activos: $($dashboard.totalLotesActivos)"
    Write-Host "Lotes agotados: $($dashboard.lotesAgotados)"
    Write-Host "Lotes stock bajo: $($dashboard.lotesStockBajo)"
    Write-Host "Stock total disponible: $($dashboard.stockTotalDisponible)"
}

Write-Host ""
Write-Host "Pruebas finales GET terminadas." -ForegroundColor Green