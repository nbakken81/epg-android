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

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TVAlarmes";
    private static final String TABLE_ALARMS = "alarmes";
    // Colunas
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_CHANNEL_SIGLA = "channelSigla";
    private static final String KEY_MINUTES_BEFORE = "minutesBefore";
    private static final String KEY_REPEATS = "repeats";

    public AlarmDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela alarmes
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_START_DATE + " DATETIME,"
                + KEY_CHANNEL_SIGLA + " TEXT,"
                + KEY_MINUTES_BEFORE + " INTEGER,"
                + KEY_REPEATS + " BOOLEAN"
                + ")";
        db.execSQL(CREATE_ALARMS_TABLE);
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
        values.put(KEY_START_DATE, "");
        values.put(KEY_CHANNEL_SIGLA, program.getChannelId());
        values.put(KEY_MINUTES_BEFORE, alarm.getMinutesBefore());
        values.put(KEY_REPEATS, !alarm.isOnce());
        // Inserir linha
        db.insert(TABLE_ALARMS, null, values);
        db.close();
    }

    public List<Alarm> findAll() {
        List<Alarm> alarmsList = new ArrayList<Alarm>();
        String selectQuery = "SELECT * FROM " + TABLE_ALARMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Criar Alarme
                int id = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                String startDate = cursor.getString(2);
                String channelSigla = cursor.getString(3);
                int minutesBefore = Integer.parseInt(cursor.getString(4));
                boolean repeats = Boolean.parseBoolean(cursor.getString(5));
                Program program = new Program(id, title, null, channelSigla);
                Alarm alarm = new Alarm(program, minutesBefore, repeats);
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
