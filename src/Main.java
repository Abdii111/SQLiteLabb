import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    private static Connection connect() {
        String url = "jdbc:sqlite:C:/Users/AbdifatahAbdulkadir/IdeaProjects/labb3.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void printActions() {
        System.out.println("\nVälj:\n");
        System.out.println("0  - Stäng av\n" +
                "1  - Visa alla lag\n" +
                "2  - Lägg till ett nytt lag\n" +
                "3  - Uppdatera ett lag\n" +
                "4  - Ta bort ett lag\n" +
                "5  - Visa alla spelare\n" +
                "6  - Lägg till en ny spelare\n" +
                "7  - Uppdatera en spelare\n" +
                "8  - Ta bort en spelare\n" +
                "9  - Visa spelare och deras lag\n" +
                "10 - Sök efter en spelare\n" +
                "11 - Markera spelare som favorit\n" +
                "12 - Visa favoriter\n" +
                "13 - Visa statistik\n");
    }

    public static void main(String[] args) {
        boolean quit = false;

        printActions();

        while (!quit) {
            System.out.println("\nVälj ett alternativ (0-13):");
            int action = scanner.nextInt();
            scanner.nextLine(); // Rensar scanner-bufferten.

            switch (action) {
                case 0:
                    quit = quitProgram();
                    break;
                case 1:
                    showAllTeams();
                    break;
                case 2:
                    inputAddTeam();
                    break;
                case 3:
                    inputUpdateTeam();
                    break;
                case 4:
                    inputDeleteTeam();
                    break;
                case 5:
                    showAllPlayers();
                    break;
                case 6:
                    inputAddPlayer();
                    break;
                case 7:
                    inputUpdatePlayer();
                    break;
                case 8:
                    inputDeletePlayer();
                    break;
                case 9:
                    showPlayersWithTeams();
                    break;
                case 10:
                    inputSearchPlayer();
                    break;
                case 11:
                    inputMarkFavorite();
                    break;
                case 12:
                    showFavorites();
                    break;
                case 13:
                    showStatistics();
                    break;
                default:
                    System.out.println("Ogiltigt val. Försök igen.");
                    break;
            }

             // Visar menyvalen i slutet av varje iteration.
        }
    }

    private static boolean quitProgram() {
        System.out.println("\nStänger ner...");
        return true;
    }

    // Metoder för Lag
    private static void inputAddTeam() {
        System.out.println("Ange lagnamn:");
        String lagNamn = scanner.nextLine();
        System.out.println("Ange stad:");
        String stad = scanner.nextLine();
        addTeam(lagNamn, stad);
    }

    private static void addTeam(String lagNamn, String stad) {
        String sql = "INSERT INTO lag(lagNamn, stad) VALUES(?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lagNamn);
            pstmt.setString(2, stad);
            pstmt.executeUpdate();
            System.out.println("Nytt lag tillagt!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void inputUpdateTeam() {
        System.out.println("Ange lagId för att uppdatera:");
        int lagId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Ange nytt lagnamn:");
        String lagNamn = scanner.nextLine();
        System.out.println("Ange ny stad:");
        String stad = scanner.nextLine();
        updateTeam(lagId, lagNamn, stad);
    }

    private static void updateTeam(int lagId, String lagNamn, String stad) {
        String sql = "UPDATE lag SET lagNamn = ?, stad = ? WHERE lagId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lagNamn);
            pstmt.setString(2, stad);
            pstmt.setInt(3, lagId);
            pstmt.executeUpdate();
            System.out.println("Lag uppdaterat.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void inputDeleteTeam() {
        System.out.println("Ange lagId för att ta bort:");
        int lagId = scanner.nextInt();
        deleteTeam(lagId);
    }

    private static void deleteTeam(int lagId) {
        String sql = "DELETE FROM lag WHERE lagId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, lagId);
            pstmt.executeUpdate();
            System.out.println("Lag borttaget.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showAllTeams() {
        String sql = "SELECT * FROM lag";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt("lagId") + "\t" +
                        rs.getString("lagNamn") + "\t" +
                        rs.getString("stad"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Metoder för Spelare
    private static void inputAddPlayer() {
        System.out.println("Ange namn:");
        String namn = scanner.nextLine();
        System.out.println("Ange position:");
        String position = scanner.nextLine();
        System.out.println("Ange lagId:");
        int lagId = scanner.nextInt();
        addPlayer(namn, position, lagId);
    }

    private static void addPlayer(String namn, String position, int lagId) {
        String sql = "INSERT INTO spelare(namn, position, lagId) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, namn);
            pstmt.setString(2, position);
            pstmt.setInt(3, lagId);
            pstmt.executeUpdate();
            System.out.println("Ny spelare tillagd.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void inputUpdatePlayer() {
        System.out.println("Ange spelareId för att uppdatera:");
        int spelareId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Ange nytt namn:");
        String namn = scanner.nextLine();
        System.out.println("Ange ny position:");
        String position = scanner.nextLine();
        System.out.println("Ange nytt lagId:");
        int lagId = scanner.nextInt();
        updatePlayer(spelareId, namn, position, lagId);
    }

    private static void updatePlayer(int spelareId, String namn, String position, int lagId) {
        String sql = "UPDATE spelare SET namn = ?, position = ?, lagId = ? WHERE spelareId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, namn);
            pstmt.setString(2, position);
            pstmt.setInt(3, lagId);
            pstmt.setInt(4, spelareId);
            pstmt.executeUpdate();
            System.out.println("Spelare uppdaterad.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void inputDeletePlayer() {
        System.out.println("Ange spelareId för att ta bort:");
        int spelareId = scanner.nextInt();
        deletePlayer(spelareId);
    }

    private static void deletePlayer(int spelareId) {
        String sql = "DELETE FROM spelare WHERE spelareId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, spelareId);
            pstmt.executeUpdate();
            System.out.println("Spelare borttagen.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showAllPlayers() {
        String sql = "SELECT * FROM spelare";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt("spelareId") + "\t" +
                        rs.getString("namn") + "\t" +
                        rs.getString("position") + "\t" +
                        rs.getInt("lagId") + "\t" +
                        rs.getInt("favorit"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Favoritfunktioner
    private static void inputMarkFavorite() {
        System.out.println("Ange spelareId för att markera som favorit:");
        int spelareId = scanner.nextInt();
        markAsFavorite(spelareId);
    }

    private static void markAsFavorite(int spelareId) {
        String sql = "UPDATE spelare SET favorit = 1 WHERE spelareId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, spelareId);
            pstmt.executeUpdate();
            System.out.println("Spelare markerad som favorit.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showFavorites() {
        String sql = "SELECT * FROM spelare WHERE favorit = 1";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt("spelareId") + "\t" +
                        rs.getString("namn") + "\t" +
                        rs.getString("position") + "\t" +
                        rs.getInt("lagId"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Statistik
    private static void showStatistics() {
        String sql = "SELECT COUNT(*) AS AntalSpelare FROM spelare";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("Antal spelare i databasen: " + rs.getInt("AntalSpelare"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Sökfunktion
    private static void inputSearchPlayer() {
        System.out.println("Ange namnet på spelaren du vill söka efter:");
        String name = scanner.nextLine();
        searchPlayer(name);
    }

    private static void searchPlayer(String namn) {
        String sql = "SELECT * FROM spelare WHERE namn LIKE ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + namn + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("spelareId") + "\t" +
                        rs.getString("namn") + "\t" +
                        rs.getString("position") + "\t" +
                        rs.getInt("lagId"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Visa spelare och deras lag (JOIN)
    private static void showPlayersWithTeams() {
        String sql = "SELECT spelare.namn AS Spelare, spelare.position AS Position, lag.lagNamn AS Lag " +
                "FROM spelare " +
                "INNER JOIN lag ON spelare.lagId = lag.lagId";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString("Spelare") + "\t" +
                        rs.getString("Position") + "\t" +
                        rs.getString("Lag"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
