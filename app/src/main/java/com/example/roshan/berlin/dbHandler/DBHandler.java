package com.example.roshan.berlin.dbHandler;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ashu on 11/6/2017.
 */

public class DBHandler extends SQLiteOpenHelper {
    Context context;

    public static final String DB_name = "Menu";
    public static final String table1 = "items";
    public static final String table2 = "home";
    public static final String table3 = "voucher";
    public static final String table4 = "user_profile";
    public static final String table_cart="cart";
    public static final String id = " _id";
    public static final String item="Artikel";
    public static final String quantity="Menge";
    public static final String price="Preis";
    public static final String type_of_menu = "menu_type";
    public static final String item_name = "name_of_item";
    public static final String item_Price="item_price";
    public static final String item_description = "description_of_item";
    public static final String image = "image1";
    public static final String date = "date1";
    public static final String discount = "discountPrice";
    public  static final String username="user_name";
    public  static final String email="email_address";
    public  static final String phone="phone_number";

    public DBHandler(Context context) {
        super(context, DB_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + table1 + "( _id integer primary key autoincrement ,menu_type text,name_of_item text ,description_of_item text,item_price text,image1 text,date1 text)");
        sqLiteDatabase.execSQL("create table " + table2 + "( _id integer primary key autoincrement ,name_of_item text ,description_of_item text,image1 text)");
        sqLiteDatabase.execSQL("create table " + table3 + "( _id integer primary key autoincrement ,item_price text ,discountPrice text,image1 text)");
        sqLiteDatabase.execSQL("create table " + table_cart + "( _id integer primary key autoincrement ,Artikel text ,Menge text,Preis text)");
        sqLiteDatabase.execSQL("create table " + table4 + "( _id integer primary key autoincrement,user_name text,email_address text ,image1 text,phone_number text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists" + table1);
        sqLiteDatabase.execSQL("drop table if exists" + table2);
        sqLiteDatabase.execSQL("drop table if exists" + table3);
        sqLiteDatabase.execSQL("drop table if exists" + table4);
        sqLiteDatabase.execSQL("drop table if exists" + table_cart);
        onCreate(sqLiteDatabase);
    }

    public void insertdata(String menutype, String itemname, String itemdescription,String price, String images, String Date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(type_of_menu, menutype);
        contentValues.put(item_name, itemname);
        contentValues.put(item_description, itemdescription);
        contentValues.put(item_Price, price);
        contentValues.put(image, images);
        contentValues.put(date, Date);
        int u = sqLiteDatabase.update(table1, contentValues, "name_of_item=?", new String[]{itemname});
        if (u == 0) {
            sqLiteDatabase.insertWithOnConflict(table1, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
       // sqLiteDatabase.insert(table1, null, contentValues);
    }

    //insert in home
    public void insertHome( String itemname, String itemdescription, String images) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(item_name, itemname);
        contentValues.put(item_description, itemdescription);
        contentValues.put(image, images);
        int u = sqLiteDatabase.update(table2, contentValues, "name_of_item=?", new String[]{itemname});
        if (u == 0) {
            sqLiteDatabase.insertWithOnConflict(table2, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    //insert in voucher
    public void insertVoucher( String price1, String discount1, String images) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(item_Price, price1);
        contentValues.put(discount, discount1);
        contentValues.put(image, images);
        int u = sqLiteDatabase.update(table3, contentValues, "item_price=?", new String[]{price1});
        if (u == 0) {
            sqLiteDatabase.insertWithOnConflict(table3, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    //insert into cart

    public void insertGutschein(String item1, String quanity1, String price1) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(item, item1);
        contentValues.put(quantity, quanity1);
        contentValues.put(price, price1);
     //   sqLiteDatabase.insert(table_cart,null,contentValues);
        int u = sqLiteDatabase.update(table_cart, contentValues, "Artikel=?", new String[]{item1});
        if (u == 0) {
            sqLiteDatabase.insertWithOnConflict(table_cart, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    //insert in user table
    public void insertUserProfile(String phoneNumber, String emailaddress, String userName, String images) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(phone, phoneNumber);
        contentValues.put(username, userName);
        contentValues.put(email, emailaddress);
        contentValues.put(image, images);
        int u = sqLiteDatabase.update(table4, contentValues, "email_address=?", new String[]{emailaddress});
        if (u == 0) {
            sqLiteDatabase.insertWithOnConflict(table4, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public Cursor getData(String menuType) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr =  db.rawQuery( "select _id as _id, menu_type, name_of_item, item_price, description_of_item, image1, date1 from items where menu_type = '"+menuType+"'",null);
        if (cr != null) {
            cr.moveToFirst();
        }
        cr.getCount();
        return cr;
    }

    public Cursor getGutschein() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr =  db.rawQuery( "select _id as _id, Artikel, Menge, Preis from cart ",null);
        if (cr != null) {
            cr.moveToFirst();
        }
        cr.getCount();
        return cr;
    }

    public Cursor getHomeData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr =  db.rawQuery( "select _id as _id, name_of_item, description_of_item, image1 from home",null);
        if (cr != null) {
            cr.moveToFirst();
        }
        cr.getCount();
        return cr;
    }

    public Cursor getVoucherDate() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr =  db.rawQuery( "select _id as _id, item_price, discountPrice, image1 from voucher",null);
        if (cr != null) {
            cr.moveToFirst();
        }
        cr.getCount();
        return cr;
    }

    public Cursor getUserProfile() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cr =  sqLiteDatabase.rawQuery( "select _id as _id, user_name, email_address, phone_number, image1 from user_profile",null);
        if (cr != null) {
            cr.moveToFirst();
        }
        if (cr != null) {
            cr.getCount();
        }
        return cr;
    }

    public Cursor total(){
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor cr =  db.rawQuery( "select sum(Preis) as total_price from cart ",null);

        if (cr != null) {
            cr.moveToFirst();
        }
        return cr;
    }

    public void delete(){
        SQLiteDatabase db=this.getWritableDatabase();
        String delete="delete from user_profile";
        db.execSQL(delete);
    }

    public void delete_gutchein(String item_name){
        SQLiteDatabase db=this.getWritableDatabase();
        String delete="delete from cart where Artikel like '"+item_name+"'";
        db.execSQL(delete);
    }

    public void deleteVoucher(){
        SQLiteDatabase db=this.getWritableDatabase();
        String delete="delete from voucher";
        db.execSQL(delete);
    }
}

