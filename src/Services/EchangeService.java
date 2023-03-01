package Services;

import Entities.Echange;
import Entities.Item;
import Entities.Livraison;
import Utils.MyDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.JDBCType.NULL;

public class EchangeService implements Services.IService<Echange> {
    Connection con;
    Statement stm;
    public EchangeService() {
        con = MyDB.getInstance().getCon();
    }
    @Override
    public void add(Echange e) {
        java.sql.Date current_date = new java.sql.Date(System.currentTimeMillis());
        String req = "INSERT INTO `echange` ( `date_echange`, `id_user1`, `id_user2`)"
                + "VALUES ('"+current_date+"', '"+e.getId_user1()+"', '"+e.getId_user2()+"')";
        try {
            stm = con.createStatement();
            stm.executeUpdate(req);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
        public List afficher()  {
            List<Echange> echanges = new ArrayList<>();

            String query="SELECT * from `echange` WHERE archived = 0";
            try {
                stm = con.createStatement();
                ResultSet result=stm.executeQuery(query);
                while (result.next()){
                    Echange e = new Echange(result.getInt("id_echange"), result.getString("titre_echange"), result.getInt("id_user1"), result.getInt("id_user2"),
                            result.getDate("date_echange"));
                    echanges.add(e);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return echanges;
    }

    @Override
    public Boolean modifier(Echange e) {
        String req = "UPDATE `echange` SET `id_user1` = '"+e.getId_user1()+"', "
                          + "`id_user2` = '"+e.getId_user2()+"' WHERE id_echange = '"+e.getId_echange()+"'";
        //String req = "UPDATE `echange` SET `date_echange` = '"+e.getSqlDate()+"' WHERE id_echange = '"+e.getId_echange()+"' ";
        try {
            stm = con.createStatement();
            int rowsUpdated = stm.executeUpdate(req);
            if (rowsUpdated >0){
                return true;
            }else {
                return false;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Boolean supprimer(Echange e) {
        String req = "UPDATE echange SET archived = 1 WHERE id_echange = '"+e.getId_echange()+"' ";
        try {
            stm = con.createStatement();
            int rowsDeleted = stm.executeUpdate(req);
            if (rowsDeleted > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    //create echange et update id_echange dans items to the id_echange created
    public void CreerEchange(Echange e, List<Item> items) {
        java.sql.Date current_date = new java.sql.Date(System.currentTimeMillis());
        int id_echange = -1;

        String req="INSERT INTO `echange` (`id_user1`, `date_echange`) VALUES ('" + e.getId_user1() + "', '"+current_date+"')";
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(req, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()){
                id_echange = result.getInt(1);
            }
            for (Item i : items) {
                stmt = con.createStatement();
                stmt.executeUpdate("UPDATE `item` SET `id_echange` = '" + id_echange + "' WHERE `id_item`='" + i.getId_item() + "'");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void ProposerEchange(Echange e, List<Item> items) {

        try {
            for (Item i : items) {
                stm = con.createStatement();
                stm.executeUpdate("UPDATE `item` SET `id_echange` = '" + e.getId_echange() + "' WHERE `id_item`='" + i.getId_item() + "'");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getUsername1(Echange e){
        String req = "SELECT `prenom` FROM `utilisateur` WHERE id_user = '"+e.getId_user1()+"'";
        try {
            stm = con.createStatement();
            ResultSet result = stm.executeQuery(req);
            if (result.next()) {
                return result.getString("prenom");
            } else {
                throw new RuntimeException("No record found");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public String getUsername2(Echange e){
        String req = "SELECT `prenom` FROM `utilisateur` WHERE id_user = '"+e.getId_user2()+"'";
        try {
            stm = con.createStatement();
            ResultSet result = stm.executeQuery(req);
            if (result.next()) {
                return result.getString("prenom");
            } else {
                throw new RuntimeException("No record found");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List afficherEchangeAccepter()  {
        List<Echange> echanges = new ArrayList<>();

        String req="SELECT * FROM `echange` WHERE archived = 0 AND id_user2 != '"+NULL+"'";
        try {
            stm = con.createStatement();
            ResultSet result=stm.executeQuery(req);
            while (result.next()){
                Echange e = new Echange(result.getInt("id_echange"), result.getString("titre_echange"), result.getInt("id_user1"), result.getInt("id_user2"),
                        result.getDate("date_echange"));
                echanges.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return echanges;
    }

    public List afficherEchangeNonAccepter()  {
        List<Echange> echanges = new ArrayList<>();

        String req="SELECT * FROM `echange` WHERE archived = 0 AND id_user2 is NULL";
        try {
            stm = con.createStatement();
            ResultSet result=stm.executeQuery(req);
            while (result.next()){
                Echange e = new Echange(result.getInt("id_echange"), result.getString("titre_echange"), result.getInt("id_user1"), result.getInt("id_user2"),
                        result.getDate("date_echange"));
                echanges.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return echanges;
    }

    public Echange getEchangeByLivraison(Livraison l) {
        String req = "SELECT * FROM echange WHERE id_echange = '"+l.getId_echange()+"'";
        try {
            stm = con.createStatement();
            ResultSet result = stm.executeQuery(req);
            if (result.next()){
                Echange e = new Echange();
                e.setId_echange(result.getInt("id_echange"));
                e.setTitre_echange(result.getString("titre_echange"));
                e.setId_user1(result.getInt("id_user1"));
                e.setId_user2(result.getInt("id_user2"));
                return e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}