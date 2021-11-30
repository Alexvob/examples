package com.astudio.progressmonitor.database;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.astudio.progressmonitor.group.Group;
import com.astudio.progressmonitor.modules.Owner;
import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.promo.Promo;
import com.astudio.progressmonitor.statistic.Statistic;
import com.astudio.progressmonitor.task.Task;
import com.astudio.progressmonitor.user.User;
import com.astudio.progressmonitor.voting.Question;


@Database(entities = {Task.class, User.class, Owner.class, Group.class, Statistic.class, Question.class,
        Plan.class, Project.class, ProjectItem.class, Promo.class},
        version = 29, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public abstract TaskDao taskDao();

    public abstract SyncTaskDao syncTaskDao();

    public abstract SyncUserDao syncUserDao();

    public abstract UserDao userDao();

    public abstract StatisticDao statisticDao();

    public abstract GroupStatisticDao groupStatisticDao();

    public abstract GroupDao groupDao();

    public abstract QuestionDao questionDao();

    public abstract PlanDao planDao();

    public abstract ProjectDao projectDao();

    public abstract PromoDao promoDao();


    // https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/540-urok-12-migracija-versij-bazy-dannyh.html
    public static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(@NonNull final SupportSQLiteDatabase database) {
            //database.execSQL("ALTER TABLE Employee ADD COLUMN birthday INTEGER DEFAULT 0 NOT NULL");
        }
    };


//    public static final Migration MIGRATION_10_11 = new Migration(10, 11) {
//        @Override
//        public void migrate(@NonNull final SupportSQLiteDatabase database) {
//            database.execSQL(
//                "CREATE TABLE IF NOT EXISTS statistic " +
//                        "(id INTEGER PRIMARY KEY NOT NULL, groupToken TEXT, year INTEGER NOT NULL, month INTEGER NOT NULL," +
//                        "allTask INTEGER NOT NULL, success INTEGER NOT NULL, canceled INTEGER NOT NULL) WITHOUT ROWID;"
//                    );
//        }
//    };


    public static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        @Override
        public void migrate(@NonNull final SupportSQLiteDatabase database) {

//            database.execSQL(
//                    "DROP TABLE IF EXISTS statistic");

//            database.execSQL(
//                "CREATE TABLE IF NOT EXISTS statistic " +
//                        "(id INTEGER PRIMARY KEY, groupToken TEXT, year INTEGER NOT NULL, month INTEGER NOT NULL," +
//                        "allTask INTEGER NOT NULL, success INTEGER NOT NULL, canceled INTEGER NOT NULL) WITHOUT ROWID;"
//                    );
        }
    };

    public static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        @Override
        public void migrate(@NonNull final SupportSQLiteDatabase database) {
//            database.execSQL(
//                "CREATE TABLE IF NOT EXISTS statistic_temp " +
//                        "(id INTEGER PRIMARY KEY, groupToken TEXT, year INTEGER NOT NULL, month INTEGER NOT NULL," +
//                        "allTask INTEGER NOT NULL , success INTEGER NOT NULL, canceled INTEGER NOT NULL);"
//                    );
//            database.execSQL(
//                    "DROP TABLE IF EXISTS statistic"
//            );
//            database.execSQL(
//                    "ALTER TABLE statistic_temp RENAME TO statistic"
//            );

        }
    };



}
