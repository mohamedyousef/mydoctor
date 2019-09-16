package com.uniyapps.yadoctor.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.uniyapps.yadoctor.Model.Message;
import com.uniyapps.yadoctor.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends DialogFragment {

    public static String TAG = "chatfragment";

    MessagesListAdapter<Message> adapter ;
    FirebaseAuth auth;
    ArrayList<Message> messages = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MessageInput messageInput = view.findViewById(R.id.input);
        MessagesList messagesList = view.findViewById(R.id.messagesList);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle("ارسال اقتراح او شكوي");

        auth = FirebaseAuth.getInstance();
        adapter = new MessagesListAdapter<Message>("your",null);
        messagesList.setAdapter(adapter);

        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                sendMsg(input.toString());
                return true;
            }
        });

        loadMesseage();

     }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    void sendMsg(String input){
        Map<String,Object> msg = new HashMap<>();
        msg.put("value",input);
        msg.put("date",new Date());
        msg.put("id","your");
        Map <String,String> map = new HashMap<String, String>();
        map.put("a","false");

        FirebaseFirestore.getInstance().collection("Chat")
                .document(auth.getUid()).collection("msg")
                .document().set(msg);

        FirebaseFirestore.getInstance().collection("Chat")
                .document(auth.getUid()).set(map);
    }
    private void loadMesseage() {
        FirebaseFirestore.getInstance().collection("Chat")
                .document(auth.getUid()).collection("msg").orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments())
                        {
                            String id = documentSnapshot.getString("id");
                            String value = documentSnapshot.getString("value");
                            Date date = documentSnapshot.getDate("date");
                            Message message = new Message(id,value,date);
                            if (!messages.contains(message)) {
                                messages.add(message);
                                adapter.addToStart(message,true);
                            }
                        }
                    }
                });
    }
}
