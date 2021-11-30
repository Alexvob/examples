package com.astudio.progressmonitor.project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.astudio.progressmonitor.modules.App;
import com.astudio.progressmonitor.modules.JSONPlaceHolderApi;
import com.astudio.progressmonitor.modules.NetworkService;
import com.astudio.progressmonitor.modules.Validator;
import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.support.RemoteDataException;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RemoteDataProject {

    private static final String TAG = "RemoteDataProject";
    private final Validator validator;
    private JSONPlaceHolderApi jsonApi;
    private RepositoryProjectCallback callback;


    public RemoteDataProject() {
        jsonApi = NetworkService.getInstance().getJSONApi();
        validator = App.getInstance().getValidator();
    }


    public interface RepositoryProjectCallback {
        void resultRemoteAddProject(Project project);
        void resultRemoteAddProject(MessageDecoder.Codes code);
        void resultRemoteUpdateProject(Project project);
        void resultRemoteUpdateProject(MessageDecoder.Codes code);
        void resultRemoteDeleteProject(Integer id);
        void resultRemoteDeleteProject(MessageDecoder.Codes code);
        void resultRemoteAddProjectItem(ProjectItem projectItem);
        void resultRemoteAddProjectItem(MessageDecoder.Codes code);
        void resultRemoteUpdateProjectItem(ProjectItem projectItem);
        void resultRemoteUpdateProjectItem(MessageDecoder.Codes code);
        void resultRemoteDeleteProjectItem(Integer id);
        void resultRemoteDeleteProjectItem(MessageDecoder.Codes code);
    }


    void setCallback(RepositoryProjectCallback callback){
        this.callback = callback;
    }


    //RepositoryProject
    void addProject(Project project){
        jsonApi
                .createNewProject(project)
                .enqueue(new Callback<Project>() {
                    @Override
                    public void onResponse(@NonNull Call<Project> call, @NonNull Response<Project> response) {
                        if ( !validator.checkResponseCode(response.code()) || !(response.body() instanceof Project) ){
                            String errorMessage = "Error-ServerCode OR remoteResponse-null OR Not Project";
                            if(response.code() == 504){
                                callback.resultRemoteAddProject(MessageDecoder.Codes.GROUP_NOT_FOUND);
                            } else {
                                try {
                                    ResponseBody errorBody = response.errorBody();
                                    if (errorBody != null) errorMessage = errorBody.string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                FirebaseCrashlytics.getInstance().recordException(new RemoteDataException(response.code(), errorMessage));
                                callback.resultRemoteAddProject(MessageDecoder.Codes.FAIL_CUT);
                            }
                        } else {
                            callback.resultRemoteAddProject(response.body());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Project> call, @NonNull Throwable t) {
                        if( t instanceof SocketTimeoutException || t instanceof UnknownHostException){
                            callback.resultRemoteAddProject(MessageDecoder.Codes.FAIL_FULL);
                            return;
                        }
                        FirebaseCrashlytics.getInstance().recordException(t);
                        callback.resultRemoteAddProject(MessageDecoder.Codes.FAIL_CUT);
                    }
                });
    }


    //RepositoryProject
    void updateProject(Project project){
        jsonApi
                .updateProject(project)
                .enqueue(new Callback<Project>() {
                    @Override
                    public void onResponse(@NonNull Call<Project> call, @NonNull Response<Project> response) {
                        if ( !validator.checkResponseCode(response.code()) || !(response.body() instanceof Project) ){
                            String errorMessage = "Error-ServerCode OR remoteResponse-null OR Not ProjectItem";
                            if(response.code() == 504){
                                callback.resultRemoteUpdateProject(MessageDecoder.Codes.GROUP_NOT_FOUND);
                            } else {
                                try {
                                    ResponseBody errorBody = response.errorBody();
                                    if (errorBody != null) errorMessage = errorBody.string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                FirebaseCrashlytics.getInstance().recordException(new RemoteDataException(response.code(), errorMessage));
                                callback.resultRemoteUpdateProject(MessageDecoder.Codes.FAIL_CUT);
                            }
                        } else {
                            callback.resultRemoteUpdateProject(response.body());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Project> call, @NonNull Throwable t) {
                        if( t instanceof SocketTimeoutException || t instanceof UnknownHostException){
                            callback.resultRemoteUpdateProject(MessageDecoder.Codes.FAIL_FULL);
                            return;
                        }
                        FirebaseCrashlytics.getInstance().recordException(t);
                        callback.resultRemoteUpdateProject(MessageDecoder.Codes.FAIL_CUT);
                    }
                });
    }


    //RepositoryProject
    void deleteProject(Project project){
        jsonApi
                .deleteProject(project.getId(), project.getGroupToken())
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        if ( !validator.checkResponseCode(response.code()) || !(response.body() instanceof Integer) ){
                            String errorMessage = "Error-ServerCode OR remoteResponse-null OR Not Integer";
                            if(response.code() == 504){
                                callback.resultRemoteDeleteProject(MessageDecoder.Codes.GROUP_NOT_FOUND);
                            } else {
                                try {
                                    ResponseBody errorBody = response.errorBody();
                                    if (errorBody != null) errorMessage = errorBody.string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                FirebaseCrashlytics.getInstance().recordException(new RemoteDataException(response.code(), errorMessage));
                                callback.resultRemoteDeleteProject(MessageDecoder.Codes.FAIL_CUT);
                            }
                        } else {
                            callback.resultRemoteDeleteProject(response.body());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                        if( t instanceof SocketTimeoutException || t instanceof UnknownHostException){
                            callback.resultRemoteDeleteProject(MessageDecoder.Codes.FAIL_FULL);
                            return;
                        }
                        FirebaseCrashlytics.getInstance().recordException(t);
                        callback.resultRemoteDeleteProject(MessageDecoder.Codes.FAIL_CUT);
                    }
                });
    }



    //RepositoryProject
    void addProjectItem(ProjectItem projectItem){
        jsonApi
                .createNewProjectItem(projectItem)
                .enqueue(new Callback<ProjectItem>() {
                    @Override
                    public void onResponse(@NonNull Call<ProjectItem> call, @NonNull Response<ProjectItem> response) {
                        if ( !validator.checkResponseCode(response.code()) || !(response.body() instanceof ProjectItem) ){
                            String errorMessage = "Error-ServerCode OR remoteResponse-null OR Not Project";
                            if(response.code() == 504){
                                callback.resultRemoteAddProjectItem(MessageDecoder.Codes.GROUP_NOT_FOUND);
                            } else {
                                try {
                                    ResponseBody errorBody = response.errorBody();
                                    if (errorBody != null) errorMessage = errorBody.string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                FirebaseCrashlytics.getInstance().recordException(new RemoteDataException(response.code(), errorMessage));
                                callback.resultRemoteAddProjectItem(MessageDecoder.Codes.FAIL_CUT);
                            }
                        } else {
                            callback.resultRemoteAddProjectItem(response.body());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ProjectItem> call, @NonNull Throwable t) {
                        if( t instanceof SocketTimeoutException || t instanceof UnknownHostException){
                            callback.resultRemoteAddProjectItem(MessageDecoder.Codes.FAIL_FULL);
                            return;
                        }
                        FirebaseCrashlytics.getInstance().recordException(t);
                        callback.resultRemoteAddProjectItem(MessageDecoder.Codes.FAIL_CUT);
                    }
                });
    }


    //RepositoryProject
    void updateProjectItem(ProjectItem projectItem){
        jsonApi
                .updateProjectItem(projectItem)
                .enqueue(new Callback<ProjectItem>() {
                    @Override
                    public void onResponse(@NonNull Call<ProjectItem> call, @NonNull Response<ProjectItem> response) {
                        if ( !validator.checkResponseCode(response.code()) || !(response.body() instanceof ProjectItem) ){
                            String errorMessage = "Error-ServerCode OR remoteResponse-null OR Not ProjectItem";
                            if(response.code() == 504){
                                callback.resultRemoteUpdateProjectItem(MessageDecoder.Codes.GROUP_NOT_FOUND);
                            } else {
                                try {
                                    ResponseBody errorBody = response.errorBody();
                                    if (errorBody != null) errorMessage = errorBody.string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                FirebaseCrashlytics.getInstance().recordException(new RemoteDataException(response.code(), errorMessage));
                                callback.resultRemoteUpdateProjectItem(MessageDecoder.Codes.FAIL_CUT);
                            }
                        } else {
                            callback.resultRemoteUpdateProjectItem(response.body());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ProjectItem> call, @NonNull Throwable t) {
                        if( t instanceof SocketTimeoutException || t instanceof UnknownHostException){
                            callback.resultRemoteUpdateProjectItem(MessageDecoder.Codes.FAIL_FULL);
                            return;
                        }
                        FirebaseCrashlytics.getInstance().recordException(t);
                        callback.resultRemoteUpdateProjectItem(MessageDecoder.Codes.FAIL_CUT);
                    }
                });
    }


    //RepositoryProject
    void deleteProjectItem(ProjectItem projectItem){
        jsonApi
                .deleteProjectItem(projectItem.getId(), projectItem.getGroupToken(), projectItem.getProjectId())
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        if ( !validator.checkResponseCode(response.code()) || !(response.body() instanceof Integer) ){
                            String errorMessage = "Error-ServerCode OR remoteResponse-null OR Not Integer";
                            if(response.code() == 504){
                                callback.resultRemoteDeleteProjectItem(MessageDecoder.Codes.GROUP_NOT_FOUND);
                            } else {
                                try {
                                    ResponseBody errorBody = response.errorBody();
                                    if (errorBody != null) errorMessage = errorBody.string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                FirebaseCrashlytics.getInstance().recordException(new RemoteDataException(response.code(), errorMessage));
                                callback.resultRemoteDeleteProjectItem(MessageDecoder.Codes.FAIL_CUT);
                            }
                        } else {
                            callback.resultRemoteDeleteProjectItem(response.body());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                        if( t instanceof SocketTimeoutException || t instanceof UnknownHostException){
                            callback.resultRemoteDeleteProjectItem(MessageDecoder.Codes.FAIL_FULL);
                            return;
                        }
                        FirebaseCrashlytics.getInstance().recordException(t);
                        callback.resultRemoteDeleteProjectItem(MessageDecoder.Codes.FAIL_CUT);
                    }
                });
    }




    // SyncServiceProject
    @SuppressWarnings("unchecked")
    Map<Integer, String> getPrimarySyncDataProject(String groupToken, int tableRow, String lastUpdated) throws IOException, RemoteDataException {
        Response remoteResponse = jsonApi.getPrimarySyncDataProject(groupToken, tableRow, lastUpdated).execute();
        if(!validator.checkResponseCode(remoteResponse.code()) || remoteResponse.body() == null || !(remoteResponse.body() instanceof Map<?,?>)){
            String errorMessage = "Response.body=null or invalid type";
            if (remoteResponse.errorBody() != null) {
                errorMessage = remoteResponse.errorBody().string();
            }
            throw new RemoteDataException(remoteResponse.code(), errorMessage);
        }
        return (Map<Integer, String>) remoteResponse.body();
    }


    // SyncServiceProject
    @SuppressWarnings("unchecked")
    List<Project> getAddedProjects(String groupToken, String listId) throws IOException, RemoteDataException {
        Response remoteResponse = jsonApi.getSecondarySyncDataProject(groupToken, listId).execute();
        if(!validator.checkResponseCode(remoteResponse.code()) || remoteResponse.body() == null || !(remoteResponse.body() instanceof List)){
            String errorMessage = "Response.body=null or invalid type";
            if (remoteResponse.errorBody() != null) {
                errorMessage = remoteResponse.errorBody().string();
            }
            throw new RemoteDataException(remoteResponse.code(), errorMessage);
        }
        return (List<Project>) remoteResponse.body();
    }


    // SyncServiceProjectItem
    @SuppressWarnings("unchecked")
    Map<Integer, String> getPrimarySyncDataProjectItem(String groupToken, int tableRow, String lastUpdated) throws IOException, RemoteDataException {
        Response remoteResponse = jsonApi.getPrimarySyncDataProjectItem(groupToken, tableRow, lastUpdated).execute();
        if(!validator.checkResponseCode(remoteResponse.code()) || remoteResponse.body() == null || !(remoteResponse.body() instanceof Map<?,?>)){
            String errorMessage = "Response.body=null or invalid type";
            if (remoteResponse.errorBody() != null) {
                errorMessage = remoteResponse.errorBody().string();
            }
            throw new RemoteDataException(remoteResponse.code(), errorMessage);
        }
        return (Map<Integer, String>) remoteResponse.body();
    }


    // SyncServiceProjectItem
    @SuppressWarnings("unchecked")
    List<ProjectItem> getAddedProjectItems(String groupToken, String listId) throws IOException, RemoteDataException {
        Response remoteResponse = jsonApi.getSecondarySyncDataProjectItem(groupToken, listId).execute();
        if(!validator.checkResponseCode(remoteResponse.code()) || remoteResponse.body() == null || !(remoteResponse.body() instanceof List)){
            String errorMessage = "Response.body=null or invalid type";
            if (remoteResponse.errorBody() != null) {
                errorMessage = remoteResponse.errorBody().string();
            }
            throw new RemoteDataException(remoteResponse.code(), errorMessage);
        }
        return (List<ProjectItem>) remoteResponse.body();
    }



}
