package rva.com.managers;

import static java.time.LocalDate.now;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import rva.com.services.TableRecordItem;

public class RecordsTableManager {
    private int maxLine;
    private ArrayList<TableRecordItem> records;
    private final Gson gson = new Gson();
    public RecordsTableManager(int maxLine) {
        this.maxLine = maxLine;
        records = new ArrayList<>();
    }

    public RecordsTableManager(int maxLine, String data) {
        this.maxLine = maxLine;
        this.records = this.fromJson(data);
    }

    /**
     * Преобразует ArrayList<TableRecordItem> в JSON-строку
     * @return JSON-представление списка записей
     */
    public String toJson() {
        return gson.toJson(records);
    }

    /**
     * Преобразует JSON-строку в ArrayList<TableRecordItem>
     * @param json JSON-строка с данными записей
     * @return список объектов TableRecordItem
     */
    public ArrayList<TableRecordItem> fromJson(String json) {
        Type listType = new TypeToken<ArrayList<TableRecordItem>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    public void addResult(int result) {
        TableRecordItem item = new TableRecordItem(result, now());
        Boolean wasInsert = false;
        for (int i = 0; i < this.records.size(); i++) {
            if (this.records.get(i).value() < result) {
                this.records.add(i, item);
                wasInsert = true;
                break;
            }
        }
        if (! wasInsert) {
            this.records.add(item);
        }
        while (this.records.size() > this.maxLine) {
              this.records.remove(this.records.size() - 1);
                                                   }
    }

    public  void dispose() {

    }

    public int size() {
        return this.records.size();
    }

    public TableRecordItem get(int i) {
        return this.records.get(i);
    }
}
