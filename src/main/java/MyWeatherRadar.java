
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Map;

public class MyWeatherRadar {

    private static int CITY_ID;
    /*
    loggers provide a better alternative to System.out.println
    https://rules.sonarsource.com/java/tag/bad-practice/RSPEC-106
     */
    private static final Logger logger = LogManager.getLogger(MyWeatherRadar.class);

    private static final Map<String, Integer> cityIds = Map.ofEntries(
            Map.entry("Aveiro", 1010500),
            Map.entry("Beja", 1020500),
            Map.entry("Braga", 1030300),
            Map.entry("Bragança", 1040200),
            Map.entry("Castelo Branco", 1050200),
            Map.entry("Coimbra", 1060300),
            Map.entry("Évora", 1070500),
            Map.entry("Faro", 1080500),
            Map.entry("Guarda", 1090700),
            Map.entry("Leiria", 1100900),
            Map.entry("Lisboa", 1110600),
            Map.entry("Portalegre", 1121400),
            Map.entry("Porto", 1131200),
            Map.entry("Santarém", 1141600),
            Map.entry("Setúbal", 1151200),
            Map.entry("Viana do Castelo", 1160900),
            Map.entry("Vila Real", 1171400),
            Map.entry("Viseu", 1182300),
            Map.entry("Funchal", 2310300),
            Map.entry("Porto Santo", 2320100),
            Map.entry("Vila do Porto", 3410100),
            Map.entry("Ponta Delgada", 3420300),
            Map.entry("Angra do Heroísmo", 3430100),
            Map.entry("Santa Cruz da Graciosa", 3440100),
            Map.entry("Velas", 3450200),
            Map.entry("Madalena", 3460200),
            Map.entry("Horta", 3470100),
            Map.entry("Santa Cruz das Flores", 3480200),
            Map.entry("Vila do Corvo", 3490100)
    );

    public static void main(String[] args) {

        CITY_ID = Integer.parseInt(args[0]);
         /*
        get a retrofit instance, loaded with the GSon lib to convert JSON into objects
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IpmaService service = retrofit.create(IpmaService.class);

        logger.info("Getting IPMA forecast for cityId {}", CITY_ID);
        Call<IpmaCityForecast> callSync = service.getForecastForACity(CITY_ID);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                logger.info( "max temp for today: " + forecast.getData().
                        listIterator().next().getTMax());
            } else {
                logger.info( "No results!");
            }
        } catch (Exception ex) {
            logger.error("Exception when getting IPMA forecast for cityId {}. Exception {}", CITY_ID, ex);
        }
    }
}
