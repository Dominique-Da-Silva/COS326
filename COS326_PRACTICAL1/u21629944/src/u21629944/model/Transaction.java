package u21629944.model;

/**
 *
 * @author Dominique Da Silva
 * u21629944
 */

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {
    // Details specified in the practical
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date transactionDate;
    private double amount;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private String transactionType; // e.g. Deposit, Withdrawal, Transfer
    
    // Constructors
    public Transaction() {}
    
    public Transaction(Date transactionDate, double amount, String senderAccountNumber, String receiverAccountNumber, String transactionType) {
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.transactionType = transactionType;
    }
    
    
    // Get and Set ID
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    // Get and Set Date
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    // Get and Set Amount
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Get and Set Sender Account
    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    // Get and Set Receiver Account
    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    // Get and Set Transaction Type
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
