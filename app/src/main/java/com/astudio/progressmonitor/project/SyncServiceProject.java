package com.astudio.progressmonitor.project;

import androidx.annotation.Keep;

import com.astudio.progressmonitor.database.AppDatabase;
import com.astudio.progressmonitor.support.RemoteDataException;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Keep
public class SyncServiceProject {

    private static final String TAG = "SyncServiceProject";
    private AppDatabase db;
    private RemoteDataProject remoteDataProject;
    private final String groupToken;


    public SyncServiceProject(AppDatabase db, RemoteDataProject remoteData, String groupToken){
        this.db = db;
        this.remoteDataProject = remoteData;
        this.groupToken = groupToken;
    }


    public boolean startSync(){
        boolean resultSync = false;

        try {
            // Sync Project

            // Если обновлений Проектов нет, то проверить обновления Пунктов проектов

            Map<Integer, String> remoteSyncData = getRemoteSyncData();
            if (!remoteSyncData.isEmpty()) {

                Map<Integer, String> localSyncData = getLocalSyncData();
                //Log.e(TAG, "remoteSyncData: " + remoteSyncData);
                //Log.e(TAG, "localSyncData: " + localSyncData);

                List<Integer> addedProjects = findAddedProject(remoteSyncData, localSyncData);
                String addedId = formingListIdForGETparams(addedProjects);
                //Log.e(TAG, "addedUsers: " + addedUsers);

                List<Integer> removableProjects = findRemovableProject(remoteSyncData, localSyncData);
                //Log.e(TAG, "removableUsers: " + removableUsers);

                if (!addedProjects.isEmpty()) {
                    List<Project> projects = remoteDataProject.getAddedProjects(groupToken, addedId);
                    //Log.e(TAG, "newUsers: " + users);
                    db.projectDao().insertProjects(projects);
                }

                db.projectDao().deleteProjects(removableProjects);
            }


            // ------------------------------------------------------------------
            // Sync ProjectItem

            Map<Integer, String> remoteSyncDataItem = getRemoteSyncDataItem();
            if (remoteSyncDataItem.isEmpty()){
                return true;
            }

            Map<Integer, String> localSyncDataItem = getLocalSyncDataItem();

            List<Integer> addedProjectItems = findAddedProject(remoteSyncDataItem, localSyncDataItem);
            String addedIdItems = formingListIdForGETparams(addedProjectItems);

            List<Integer> removableProjectItems = findRemovableProject(remoteSyncDataItem, localSyncDataItem);

            if (!addedProjectItems.isEmpty()){
                List<ProjectItem> projectItems = remoteDataProject.getAddedProjectItems(groupToken, addedIdItems);
                db.projectDao().insertProjectItems(projectItems);
            }

            db.projectDao().deleteProjectItems(removableProjectItems);

            resultSync = true;

        } catch (IOException | RemoteDataException e) {
            if( e instanceof SocketTimeoutException || e instanceof UnknownHostException){
                //FirebaseCrashlytics.getInstance().recordException(e);
            } else {
                //remoteDataUser.loggingToServer(new ErrorMessage(e.getStackTrace(), App.getInstance().getCurrentUserId(), e.getMessage()));
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }
        return resultSync;
    }


    private  Map<Integer, String> getRemoteSyncData() throws IOException, RemoteDataException {
        int rowProject = db.projectDao().getRowProject(groupToken);
        String localLastUpdatedAt = db.projectDao().getLastUpdatedLocalProject(groupToken);
        return remoteDataProject.getPrimarySyncDataProject(groupToken, rowProject, localLastUpdatedAt);
    }


    private  Map<Integer, String> getRemoteSyncDataItem() throws IOException, RemoteDataException {
        int rowProjectItem = db.projectDao().getRowProjectItem(groupToken);
        String localLastUpdatedAt = db.projectDao().getLastUpdatedLocalProjectItem(groupToken);
        return remoteDataProject.getPrimarySyncDataProjectItem(groupToken, rowProjectItem, localLastUpdatedAt);
    }


    private Map<Integer, String> getLocalSyncData() {
        List<ProjectEasy> localListEasy = db.projectDao().getAllUpdatedAt(groupToken);
        return convertListToMap(localListEasy);
    }


    private Map<Integer, String> getLocalSyncDataItem() {
        List<ProjectEasy> localListEasy = db.projectDao().getAllUpdatedAtItem(groupToken);
        return convertListToMap(localListEasy);
    }


    private Map<Integer, String> convertListToMap(List<ProjectEasy> easyList){
        Map<Integer, String> treeMap = new TreeMap<>();
        for(ProjectEasy easy : easyList){
            treeMap.put(easy.getId(), easy.getUpdatedAt());
        }
        return treeMap;
    }


    private List<Integer> findAddedProject (Map<Integer, String> remoteMap, Map<Integer, String> localMap){
        List<Integer> addedIds = new ArrayList<>();
        for(Map.Entry <Integer, String> remoteEntry : remoteMap.entrySet()){
            if(!localMap.containsKey(remoteEntry.getKey()) || !remoteEntry.getValue().equals(localMap.get(remoteEntry.getKey())) ){
                addedIds.add(remoteEntry.getKey());
            }
        }
        return addedIds;
    }


    private List<Integer> findRemovableProject(Map<Integer, String> remoteMap, Map<Integer, String> localMap) {
        List<Integer> removableIds = new ArrayList<>();
        for(Map.Entry <Integer, String> localEntry : localMap.entrySet()){
            if(!remoteMap.containsKey(localEntry.getKey()) ){
                removableIds.add(localEntry.getKey());
            }
        }
        return removableIds;
    }


    private String formingListIdForGETparams(List<Integer> listIdForAdd){
        return listIdForAdd.toString().replace(" ", "").replace("[", "").replace("]", "");
    }



}
