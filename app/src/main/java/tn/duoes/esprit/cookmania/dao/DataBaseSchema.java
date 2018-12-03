package tn.duoes.esprit.cookmania.dao;

public class DataBaseSchema {
    public static final class FavoriteTable {
        public static final String NAME = "favorites";

        public static final class Cols{
            public static final String ID = "id";
            public static final String USER_ID = "user_id";
            public static final String RECIPE_ID = "recipe_id";
        }
    }
}
