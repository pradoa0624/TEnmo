package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDAO {
    List<Transfer> getTransfers(int id);

    List<Transfer> getTransfersByAccountFrom(int id);

    List<Transfer> getTransfersByAccountTo(int id);

    Transfer getTransferByTransferId(int id);

    Transfer createTransfer(Transfer transfer);

    Transfer updateTransferStatus(Transfer transfer);

}
