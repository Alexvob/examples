package com.astudio.progressmonitor.interfaces;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.support.MessageDecoder;

import java.util.List;

public interface PromoContracts {



    interface Presenter{
        //void resultGetPromoImage(Bitmap bm, String fileName);
        void resultCreatePromo(MessageDecoder.Codes code);
        void resultDeletePromo(MessageDecoder.Codes code);
    }


    interface View{
        void successDialog(MessageDecoder.Codes code);
        void failDialog(MessageDecoder.Codes code);
    }


    interface Repository{
        //void resultLocalBindPost(int result);
    }


}
