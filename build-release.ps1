param(
    [string]$JavaHome = $(if ($env:JAVA_HOME) { $env:JAVA_HOME } else { 'C:\Program Files\Zulu\zulu-21' }),
    [Parameter(Mandatory = $true)]
    [string]$KeystorePath,
    [string]$KeyAlias = 'fcl-driver-release',
    [string]$PasswordFile,
    [string]$SdkPath,
    [string]$GradleUserHome
)

$ErrorActionPreference = 'Stop'
if (-not (Test-Path -LiteralPath $KeystorePath -PathType Leaf)) { throw 'Keystore file not found.' }
if (-not (Test-Path -LiteralPath (Join-Path $JavaHome 'bin\java.exe'))) { throw 'JDK not found.' }

$names = @('JAVA_HOME', 'ANDROID_HOME', 'GRADLE_USER_HOME', 'FCL_KEYSTORE', 'FCL_STORE_PASSWORD', 'FCL_KEY_ALIAS', 'FCL_KEY_PASSWORD')
$previous = @{}
foreach ($name in $names) { $previous[$name] = [Environment]::GetEnvironmentVariable($name, 'Process') }
try {
    if ($PasswordFile) {
        $password = [IO.File]::ReadAllText((Resolve-Path -LiteralPath $PasswordFile)).TrimEnd("`r", "`n")
    } else {
        $secure = Read-Host 'Keystore password (also used as key password)' -AsSecureString
        $pointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secure)
        try { $password = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($pointer) }
        finally { [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($pointer) }
    }
    if ([string]::IsNullOrEmpty($password)) { throw 'Password is empty.' }
    $env:JAVA_HOME = $JavaHome
    if ($SdkPath) { $env:ANDROID_HOME = $SdkPath }
    if ($GradleUserHome) { $env:GRADLE_USER_HOME = $GradleUserHome }
    $env:FCL_KEYSTORE = (Resolve-Path -LiteralPath $KeystorePath).Path
    $env:FCL_STORE_PASSWORD = $password
    $env:FCL_KEY_ALIAS = $KeyAlias
    $env:FCL_KEY_PASSWORD = $password
    Push-Location $PSScriptRoot
    try {
        & .\gradlew.bat :app:assembleRelease :app:lintRelease --no-daemon --console=plain
        if ($LASTEXITCODE -ne 0) { throw "Gradle failed with exit code $LASTEXITCODE" }
    } finally { Pop-Location }
} finally {
    foreach ($name in $names) { [Environment]::SetEnvironmentVariable($name, $previous[$name], 'Process') }
    $password = $null
}
