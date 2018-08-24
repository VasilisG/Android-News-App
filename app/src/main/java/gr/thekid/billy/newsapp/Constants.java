package gr.thekid.billy.newsapp;

public class Constants {

    public static final String SUBJECT = "SUBJECT";
    public static final String REPOSITORY_URL = "";
    public static final String SHARE_MESSAGE = "Share via";

    public static final String API_KEY = "";
    public static final String NEWS_SOURCES_URL = "https://newsapi.org/v2/sources?apiKey=";
    public static final String TOP_HEADLINES_URL = "https://newsapi.org/v2/top-headlines?";

    public static final String TEST_CONNECTION_ADDRESS = "8.8.8.8";
    public static final String NETWORK_IS_DOWN_MESSAGE = "Network is down. Please check your connection.";
    public static final String NO_RECENT_HEADLINES = "There are no headlines to delete.";

    public static final String DB_NAME = "TwinkleDatabase";
    public static final int DB_VERSION = 1;
    public static final String FAVOURITE_SOURCES_TABLE = "FAVOURITE_SOURCES";
    public static final String RECENT_HEADLINES_TABLE = "RECENT_HEADLINES";
    public static final int MAX_HEADLINE_RECORDS = 10;

    public static final int DIP_CATEGORY_ALERT_DIALOG = 69;
    public static final int TEST_CONNECTION_PORT = 53;

    public static final int ADD_TO_FAVOURITES_ITEM_POSITION = 1;
    public static final int REMOVE_FROM_FAVOURITES_ITEM_POSITION = 2;

    public static final String LOADING_SOURCES_DIALOG_MESSAGE = "Loading news sources";
    public static final String LOADING_HEADLINES_DIALOG_MESSAGE = "Loading headlines";
    public static final String CONFIRM_DELETION_OF_ALL_RECORDS_MESSAGE = "Are you sure you want to delete all recent headlines?";
    public static final String CONFIRM_DELETION_OF_RECORD = "Are you sure you want to delete this headline?";
    public static final String POSITIVE_DELETE_ANSWER = "Delete";
    public static final String POSITIVE_ANSWER = "Delete all";
    public static final String CANCEL = "Cancel";

    public static final String JSON_STATUS_OK = "ok";
    public static final String HEADLINE_URL_KEY = "headlineUrlString";
    public static final String NEWS_SOURCE_KEY = "newsSource";
    public static final String ARTICLE_URL_KEY = "articleUrl";

    public static final String EMPTY_STRING = "";
    public static final String NULL_STRING = "null";

    public static final String MISSING_TITLE_MESSAGE = "<Title is missing>";
    public static final String MISSING_DESCRIPTION_MESSAGE = "<Description is missing>";
    public static final String MISSING_DATE_MESSAGE = "<Date is missing>";
}
