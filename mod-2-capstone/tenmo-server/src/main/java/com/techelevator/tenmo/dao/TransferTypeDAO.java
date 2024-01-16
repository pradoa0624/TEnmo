package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class TransferTypeDAO {
    private final JdbcTemplate jdbcTemplate;

    public TransferTypeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TransferType> getTransferTypes(){
        List<TransferType> transferTypes = new ArrayList<>();
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()){
                TransferType transferType = mapRowToTransferType(results);
                transferTypes.add(transferType);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferTypes;
    }

    public TransferType getTransferTypeById(int id) {
        TransferType transferType = null;
        String sql = "SELECT transfer_type_id, transfer_type_desc " +
                "FROM transfer_type WHERE transfer_type_id = ? ";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()){
                transferType = mapRowToTransferType(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferType;
    }

    public TransferType getTransferTypeByDesc(String desc) {
        //Convert first letter to capital in case of mistaken lower case.
        String capDesc = desc.substring(0, 1).toUpperCase() + desc.substring(1);

        TransferType transferType = null;

        String sql = "SELECT transfer_type_id, transfer_type_desc " +
                "FROM transfer_type WHERE transfer_type_desc = ? ";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, capDesc);
            if (results.next()){
                transferType = mapRowToTransferType(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferType;
    }

    private TransferType mapRowToTransferType(SqlRowSet rs){
        TransferType transferType = new TransferType();
        transferType.setTransferTypeId(rs.getInt("transfer_type_id"));
        transferType.setTransferTypeDesc(rs.getString("transfer_type_desc"));
        return transferType;
    }
}
