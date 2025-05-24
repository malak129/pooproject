package model;

public class User {

     private int id;
        private String username;
        private String password;
        private int score;

        // Constructeurs, getters et setters
        public User(int id, String username, String password, int score) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.score = score;
        }

        public User(String username, String password) {
            this.username = username;
            this.password = password;
            this.score = 0;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
}
	
