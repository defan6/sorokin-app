$services = @("gateway", "auth", "event-manager", "event-notificator")

foreach ($service in $services) {
    Write-Host "Building $service ..."
    Set-Location $service
    mvn clean package -DskipTests
    Set-Location ..
}

Write-Host "All services are built successfully!!!!1"
