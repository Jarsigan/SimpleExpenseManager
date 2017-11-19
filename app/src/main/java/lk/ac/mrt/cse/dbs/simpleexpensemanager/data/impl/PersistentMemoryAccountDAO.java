package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

/**
 * Created by jarsi on 19-Nov-17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class PersistentMemoryAccountDAO extends SQLiteOpenHelper implements AccountDAO {
    //private final Map<String, Account> accounts;

    public static final String DATABASE_NAME = "150242N.db";
    public static final String CONTACTS_COLUMN_NO = "accountno";
    public static final String CONTACTS_COLUMN_BANK_NAME = "bankname";
    public static final String CONTACTS_COLUMN_HOLDER_NAME = "holdername";
    public static final String CONTACTS_COLUMN_BALANCE = "balance";

    public PersistentMemoryAccountDAO(Context context) {
        super(context, DATABASE_NAME , null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table account " +
                "(accountno text primary key, bankname text,holdername text,balance double)"
        );
        db.execSQL(
                "create table TBLtrans " +
                        "(accountno text, type text, date BLOB , amount double)"
        );

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS account");
        onCreate(db);
    }




    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NO)));
            res.moveToNext();
        }
        return array_list;


    }

    @Override
    public List<Account> getAccountsList()
    {
        ArrayList<Account> array_list = new ArrayList<Account>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String accountNo = res.getString(res.getColumnIndex(CONTACTS_COLUMN_NO));
            String bankName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_BANK_NAME));
            String accountHolderName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_HOLDER_NAME));
            Double balance = res.getDouble(res.getColumnIndex(CONTACTS_COLUMN_BALANCE));

            array_list.add(new Account(accountNo,bankName,accountHolderName,balance));
            res.moveToNext();
        }
        return array_list;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account where id="+accountNo+"", null );

        String accountno = res.getString(res.getColumnIndex(CONTACTS_COLUMN_NO));
        String bankName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_BANK_NAME));
        String accountHolderName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_HOLDER_NAME));
        Double balance = res.getDouble(res.getColumnIndex(CONTACTS_COLUMN_BALANCE));

        return  new Account(accountno,bankName,accountHolderName,balance);


    }

    @Override
    public void addAccount(Account account) {
        String accountNo = account.getAccountNo();
        String bankName = account.getBankName();
        String holderName = account.getAccountHolderName();
        Double balance = account.getBalance();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountno", accountNo);
        contentValues.put("bankname", bankName);
        contentValues.put("holdername", holderName);
        contentValues.put("balance", balance);

        db.insert("account", null, contentValues);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("account",
                "accountno = ? ",
                new String[] { accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

    }
}
