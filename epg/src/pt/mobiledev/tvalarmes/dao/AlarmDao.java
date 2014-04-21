package pt.mobiledev.tvalarmes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import pt.mobiledev.tvalarmes.domain.Alarm;
import pt.mobiledev.tvalarmes.domain.Channel;
import pt.mobiledev.tvalarmes.domain.Program;

public class AlarmDao extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 3;
    static final String DATABASE_NAME = "TVAlarmes";
    static final String TABLE_ALARMS = "alarmes";
    // Colunas
    static final String KEY_TITLE = "title";
    static final String KEY_CHANNEL_SIGLA = "channelSigla";
    static final String KEY_REPEATS = "repeats";

    public AlarmDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS + "( "
                + KEY_TITLE + " TEXT, "
                + KEY_CHANNEL_SIGLA + " TEXT, "
                + KEY_REPEATS + " BOOLEAN, "
                + " PRIMARY KEY (" + KEY_TITLE + ", " + KEY_CHANNEL_SIGLA + ") "
                + " )";
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
        values.put(KEY_TITLE, program.getTitle());
        values.put(KEY_CHANNEL_SIGLA, program.getChannelId());
        values.put(KEY_REPEATS, !alarm.isOnce());
        try {
            db.insertOrThrow(TABLE_ALARMS, null, values); // Inserir linha
        } catch (SQLiteConstraintException e) {
            // não faz nada... já existe
        }
        db.close();
    }

    public List<Alarm> findByChannel(Channel channel) {
        List<Alarm> alarmsList = new ArrayList<Alarm>();
        String selectQuery = "SELECT * FROM " + TABLE_ALARMS
                + (channel == null ? " " : " WHERE " + KEY_CHANNEL_SIGLA + " = '" + channel.getId() + "'")
                + " ORDER BY " + KEY_CHANNEL_SIGLA + ", " + KEY_TITLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                String channelSigla = cursor.getString(1);
                boolean repeats = Boolean.parseBoolean(cursor.getString(2));
                Program program = new Program(title, channelSigla);
                Alarm alarm = new Alarm(program, repeats);
                alarmsList.add(alarm);
            } while (cursor.moveToNext());
        }
        db.close();
        return alarmsList;
    }

    public List<Alarm> findAll() {
        return findByChannel(null);
    }

    public List<Channel> getAllChannels() {
        String selectQuery = "SELECT DISTINCT " + KEY_CHANNEL_SIGLA + " FROM " + TABLE_ALARMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Channel> channels = new ArrayList<Channel>();
        if (cursor.moveToFirst()) {
            do {
                channels.add(new Channel(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        db.close();
        return channels;
    }

    public void delete(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        Program program = alarm.getProgram();
        db.delete(TABLE_ALARMS, KEY_TITLE + " = ? "
                + " AND " + KEY_CHANNEL_SIGLA + " = ? ",
                new String[]{
                    program.getTitle(), program.getChannelId()
                });
        db.close();
    }
}
