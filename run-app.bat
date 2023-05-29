java -DPOSTGRE_USERNAME="postgres" ^
     -DPOSTGRE_PASSWORD="genadi1993" ^
     -DPOSTGRE_URL="jdbc:postgresql://localhost:5432/weather_api?currentSchema=public" ^
     -DCHANGE_LOG_FILE="changelog-master-jar.yaml" ^
     -DIP_GEOLOCATION_API_KEY="17b1853e872a490eb0b38f3a3ab42aac" ^
     -DWEATHER_API_KEY="64aa1425a3fe4291aad113411232505" ^
        -jar ./target/weather-app.jar