package com.astudio.progressmonitor.interfaces;

import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.voting.Question;

public interface VotingContracts {

    interface Presenter{
        void resultCreateQuestion(Question question);
        void failCreateQuestion(MessageDecoder.Codes code);
    }


    interface PresenterDesc{
        void resultSetVote(MessageDecoder.Codes code);

    }

    interface View{
        void gotoQuestionDescActivity(Question question);
        void failResult(MessageDecoder.Codes code);
    }

    interface ViewDesc{
        void resultSetVote(MessageDecoder.Codes code);
    }

    interface AdapterRecyclerViewCallback{
        void transferSelectedPosFromRecyclerView(int position);
    }


    interface Repository{
        void resultInsertQuestion(Question question);
    }

}
