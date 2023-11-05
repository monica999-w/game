package auth;
import java.sql.*;

public class ConnectionDB {
    private Connection connection;
    private String url;
    private String username;
    private String password;

    public ConnectionDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Conexiune reușită!");
        } catch (SQLException e) {
            System.out.println("Eroare la conectarea la baza de date: " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Eroare la autentificare: " + e.getMessage());
            return false;
        }
    }

    public boolean register(String username, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (username, password, score) VALUES (?, ?, 0)"); // Setăm score-ul la 0 la înregistrare
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Eroare la înregistrare: " + e.getMessage());
            return false;
        }
    }



    public int getUserId(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE username = ?");
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Eroare la obținerea id-ului utilizatorului: " + e.getMessage());
        }

        return -1;
    }


    public int getScore(int userId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT score FROM users WHERE id = ?");
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("score");
            }
        } catch (SQLException e) {
            System.out.println("Eroare la obținerea scorului: " + e.getMessage());
        }
        return 0;
    }

    public boolean updateScore(int userId, int newScore) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET score = ? WHERE id = ?");
            preparedStatement.setInt(1, newScore);
            preparedStatement.setInt(2, userId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea scorului: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Eroare la închiderea conexiunii: " + e.getMessage());
        }
    }

    public void listUsers() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Username: " + resultSet.getString("username"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Eroare la listarea utilizatorilor: " + e.getMessage());
        }
    }
}
