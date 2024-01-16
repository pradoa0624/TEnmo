package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class TransferStatusDAO {
    private final JdbcTemplate jdbcTemplate;

    public TransferStatusDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TransferStatus> getTransferStatuses(){
        List<TransferStatus> transferStatuses = new ArrayList<>();
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()){
                TransferStatus transferStatus = mapRowToTransferStatus(results);
                transferStatuses.add(transferStatus);
            }
        } catch(CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to derver or database.", e);
        }
        return transferStatuses;
    }

    public TransferStatus getTransferStatusById(int id){
        TransferStatus transferStatus = null;
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status " +
                "WHERE transfer_status_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            if(results.next()){
                transferStatus = mapRowToTransferStatus(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database.", e);
        }
        return transferStatus;
    }

    public TransferStatus getTransferStatusByDesc(String desc){
        //Convert first letter to capital in case of mistaken lower case
        String capDesc = desc.substring(0, 1).toUpperCase() + desc.substring(1);

        TransferStatus transferStatus = null;
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status " +
                "WHERE transfer_status_desc = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            if(results.next()){
                transferStatus = mapRowToTransferStatus(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database.", e);
        }
        return transferStatus;
    }

    private TransferStatus mapRowToTransferStatus(SqlRowSet rs){
        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setTransferStatusId(rs.getInt("transfer_status_id"));
        transferStatus.setTransferStatusDesc(rs.getString("transfer_status_desc"));
        return transferStatus;

    }
}
