package pt.mobiledev.tvalarmes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import pt.mobiledev.tvalarmes.domain.Alarm;
import pt.mobiledev.tvalarmes.domain.Program;

public class AlarmDao extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "TVAlarmes";
    static final String TABLE_ALARMS = "alarmes";
    // Colunas
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_CHANNEL_SIGLA = "channelSigla";
    static final String KEY_REPEATS = "repeats";

    public AlarmDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_CHANNEL_SIGLA + " TEXT,"
                + KEY_REPEATS + " BOOLEAN )";
        db.execSQL(CREATE_ALARMS_TABLE);  // Criação da tabela alarmes
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }

    public void add(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Program program = alarm.getProgram();
        values.put(KEY_ID, program.getId());
        values.put(KEY_TITLE, program.getTitle());
        values.put(KEY_CHANNEL_SIGLA, program.getChannelId());
        values.put(KEY_REPEATS, !alarm.isOnce());
        db.insert(TABLE_ALARMS, null, values); // Inserir linha
        db.close();
    }

    public List<Alarm> findAll() {
        List<Alarm> alarmsList = new ArrayList<Alarm>();
        String selectQuery = "SELECT * FROM " + TABLE_ALARMS
                + " ORDER BY " + KEY_CHANNEL_SIGLA + ", " + KEY_TITLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {   // Criar Alarme
                int id = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                String channelSigla = cursor.getString(2);
                boolean repeats = Boolean.parseBoolean(cursor.getString(3));
                Program program = new Program(id, title, null, channelSigla);
                Alarm alarm = new Alarm(program, repeats);
                alarmsList.add(alarm);
            } while (cursor.moveToNext());
        }
        return alarmsList;
    }

    public void delete(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        Program program = alarm.getProgram();
        db.delete(TABLE_ALARMS, KEY_ID + " = ?",
                new String[]{String.valueOf(program.getId())});
        db.close();
    }
}
