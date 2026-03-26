$body = @{
    contents = @(
        @{
            parts = @(
                @{ text = "hi" }
            )
        }
    )
} | ConvertTo-Json -Depth 10

try {
    $response = Invoke-RestMethod -Uri "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyClsP9bt2VFXqbEYBPx9NlbBVwP0iOVHEY" -Method Post -Body $body -ContentType "application/json"
    $response | ConvertTo-Json -Depth 10
} catch {
    $_.Exception.Response.StatusCode.value__
    $reader = new-object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $reader.ReadToEnd()
}
