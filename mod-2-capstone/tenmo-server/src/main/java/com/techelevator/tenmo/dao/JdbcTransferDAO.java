package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.jboss.logging.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.techelevator.util.CustomLogger;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO{

    private final JdbcTemplate jdbcTemplate;
    private static final CustomLogger logger = new CustomLogger(JdbcTransferDAO.class);

    //Created a longer SQL statement that better joins the tables
    //account_from = af
    //account_to = aTo
    private final String sqlTransfer = "SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, t.amount, " +
            "af.account_id as fromAcct, af.user_id as fromUser, af.balance as fromBal, " +
            "aTo.account_id as toAcct, aTo.user_id as toUser, aTo.balance as toBal " +
            "FROM transfer t " +
            "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id "+
            "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id "+
            "JOIN account af on account_from = af.account_id " +
            "JOIN account aTo on account_to = aTo.account_id ";

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransfers(int id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
        "FROM transfer WHERE account_to = ? OR account_from = ? ";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
            while(results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }



    @Override
    public List<Transfer> getTransfersByAccountFrom(int id){
        List<Transfer> transfers =  new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE account_from = ? ";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;

    }

    @Override
    public List<Transfer> getTransfersByAccountTo(int id){
        List<Transfer> transfers =  new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE account_to = ? ";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;

    }

    @Override
    public Transfer getTransferByTransferId(int id) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE transfer_id = ? ";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()){
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        Transfer newTransfer = null;
        //create transfer
        System.out.println("from the DAO method: " + transfer);
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        try {
            Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(),
                    transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

            if (newTransferId != null) {
                int intTransferId = newTransferId.intValue();
                newTransfer = getTransferByTransferId(intTransferId);
            } else {
                logger.error("No data returned by the query.");
            }

        } catch (EmptyResultDataAccessException e) {
            logger.log(CustomLogger.Level.ERROR, "No data returned by the query.", e);
        } catch (CannotGetJdbcConnectionException e) {
            logger.log(CustomLogger.Level.ERROR, "Unable to connect to server or database", e);
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            logger.log(CustomLogger.Level.ERROR, "Data integrity violation", e);
            throw new DaoException("Data integrity violation. SQL Query: " + sql, e);

        } catch (Exception e) {
            logger.log(CustomLogger.Level.ERROR, "An unexpected error occurred.", e);
            throw new DaoException("An unexpected error occurred.", e);
        }
        return newTransfer;
    }

    @Override
    public Transfer updateTransferStatus(Transfer transfer) {
        Transfer updatedTransfer = null;
        String sql = "UPDATE transfer Set transfer_status_id = ? WHERE transfer_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
            if (rowsAffected == 0){
                throw new DaoException("Zero rows affected, expected at least one.");
            }
            updatedTransfer = getTransferByTransferId(transfer.getTransferId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedTransfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
