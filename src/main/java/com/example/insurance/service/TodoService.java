package com.example.insurance.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.insurance.FirebaseConfig;
import com.example.insurance.model.Todo;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final FirebaseConfig firebase;

    private CollectionReference collection() {
        return firebase.getDb().collection("todos");
    }

    public List<Todo> getAll() throws Exception {
        List<Todo> list = new ArrayList<>();
        for (DocumentSnapshot d : collection().get().get().getDocuments()) {
            Todo t = d.toObject(Todo.class);
            if (t != null) {
                t.setId(d.getId());
                list.add(t);
            }
        }
        return list;
    }

    public Todo create(String title) throws Exception {
        DocumentReference ref = collection().document();
        Todo todo = new Todo(ref.getId(), title, false, new Date());
        ref.set(todo).get();
        return todo;
    }

    public void delete(String id) {
        collection().document(id).delete();
    }

    public void toggle(String id, boolean completed) throws Exception {
        collection().document(id).update("completed", completed).get();
    }
}

