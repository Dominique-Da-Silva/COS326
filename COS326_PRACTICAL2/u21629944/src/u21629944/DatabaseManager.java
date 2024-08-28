package u21629944;

import javax.persistence.*;
import java.util.List;

public class DatabaseManager {
    private EntityManagerFactory emf;
    private EntityManager em;

    public DatabaseManager() {
        emf = Persistence.createEntityManagerFactory("$objectdb/db/u21629944.odb");
        em = emf.createEntityManager();
    }

    public void close() {
        em.close();
        emf.close();
    }

    public void saveBankAccount(BankAccount account) {
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
    }

    public void saveTransaction(Transaction transaction) {
        em.getTransaction().begin();
        BankAccount account = transaction.getBankAccount();
        if (account != null) {
            account = em.merge(account);
            account.addTransaction(transaction);
        }
        em.persist(transaction);
        em.getTransaction().commit();
    }

    public BankAccount findBankAccount(String accountNumber) {
        return em.find(BankAccount.class, accountNumber);
    }

    public List<Transaction> findTransactions(String accountNumber) {
        TypedQuery<Transaction> query = em.createQuery(
            "SELECT t FROM Transaction t WHERE t.senderAccountNumber = :accountNumber OR t.receiverAccountNumber = :accountNumber", 
            Transaction.class);
        query.setParameter("accountNumber", accountNumber);
        return query.getResultList();
    }


    public void deleteBankAccount(String accountNumber) {
        em.getTransaction().begin();
        BankAccount account = findBankAccount(accountNumber);
        if (account != null) {
            em.remove(account);
        }
        em.getTransaction().commit();
    }

    public List<Transaction> findAllTransactions() {
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t", Transaction.class);
        return query.getResultList();
    }

    public List<BankAccount> findAllBankAccounts() {
        return em.createQuery("SELECT b FROM BankAccount b", BankAccount.class).getResultList();
    }

    public void deleteBankAccountAndTransactions(String accountNumber) {
        em.getTransaction().begin();
        BankAccount account = findBankAccount(accountNumber);
        if (account != null) {
            // Deleting all transactions associated with the bank account
            TypedQuery<Transaction> query = em.createQuery(
                "SELECT t FROM Transaction t WHERE t.senderAccountNumber = :accountNumber OR t.receiverAccountNumber = :accountNumber", 
                Transaction.class);
            query.setParameter("accountNumber", accountNumber);
            List<Transaction> transactions = query.getResultList();
            for (Transaction transaction : transactions) {
                em.remove(transaction);
            }

            // Deleting the bank account
            em.remove(account);
        }
        em.getTransaction().commit();
    }


    public List<Transaction> getAllTransactions() {
        return findAllTransactions();
    }

    public List<BankAccount> getAllBankAccounts() {
        return findAllBankAccounts();
    }
}
