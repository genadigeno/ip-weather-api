java -DPOSTGRE_USERNAME="<DB_NAME>" ^
     -DPOSTGRE_PASSWORD="<DB_PWD>" ^
     -DPOSTGRE_URL="jdbc:postgresql://<DB_HOST>:5432/<DB_NAME>?currentSchema=public" ^
     -DCHANGE_LOG_FILE="changelog-master-jar.yaml" ^
     -DIP_GEOLOCATION_API_KEY="<GEO_API_KEY>" ^
     -DWEATHER_API_KEY="<WEATHER_API_KEY>" ^
        -jar ./target/weather-app.jar
