package io.pivotal.android.data.sample.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.client.AuthorizedResourceClient;
import io.pivotal.android.data.data.DataListener;
import io.pivotal.android.data.data.DataObject;
import io.pivotal.android.data.sample.R;
import io.pivotal.android.data.sample.view.EditorCell;
import io.pivotal.android.data.util.Logger;

public class DataEditorActivity extends ActionBarActivity {

    private static final String MY_DATA_OBJECT = "MY_DATA_OBJECT";
    private static final String CLASS_NAME = "objects";
    private static final String OBJECT_ID = "123";
    private static final String LABEL_KEY = "Key";
    private static final String LABEL_VALUE = "Value";
    private static final String LABEL_CLASS_NAME = "Class Name";
    private static final String LABEL_OBJECT_ID = "Object ID";

    private DataObject dataObject;
    private ScrollView scrollView;
    private LinearLayout container;
    private EditorCell headerCell;
    private List<EditorCell> editorCells;

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View v) {
            if (v instanceof EditorCell) {
                final EditorCell cell = (EditorCell) v;
                final AlertDialog dialog = new AlertDialog.Builder(DataEditorActivity.this)
                        .setTitle("Delete item with key '" + cell.getValue1() + "'?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(v);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.setup(this);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_data_editor);
        if (savedInstanceState != null) {
            dataObject = savedInstanceState.getParcelable(MY_DATA_OBJECT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupViews();
        if (dataObject == null) {
            clearItems();
            fetchObject();
        } else {
            populateContainer();
        }
    }

    private void setupViews() {
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        container = (LinearLayout) findViewById(R.id.container);
    }

    private void clearItems() {
        dataObject = new DataObject(CLASS_NAME);
        dataObject.setObjectId(OBJECT_ID);
        populateContainer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveObject();
            return true;
        } else if (id == R.id.action_fetch) {
            fetchObject();
            return true;
        } else if (id == R.id.action_add_item) {
            addItem();
            return true;
        } else if (id == R.id.action_delete_item) {
            deleteModeHint();
            return true;
        } else if (id == R.id.action_delete_object) {
            deleteObjectConfirmation();
            return true;
        } else if (id == R.id.action_clear_items) {
            clearItems();
            return true;
        } else if (id == R.id.action_view_json) {
            viewJson();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewJson() {
        updateObject();
        final Intent i = new Intent(this, ViewJsonActivity.class);
        i.putExtra(ViewJsonActivity.MY_DATA_OBJECT, dataObject);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateObject();
        outState.putParcelable(MY_DATA_OBJECT, dataObject);
    }

    private void fetchObject() {
        updateObject();
        setProgressBar(true);
        try {
            final AuthorizedResourceClient client = DataStore.getInstance().getClient(this);
            dataObject.fetch(client, new DataListener() {

                @Override
                public void onSuccess(final DataObject object) {
                    setProgressBar(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DataEditorActivity.this, "Fetched object.", Toast.LENGTH_LONG).show();
                            populateContainer();
                        }
                    });
                }

                @Override
                public void onUnauthorized(DataObject object) {
                    setProgressBar(false);
                    showToast("Authorization error fetching object");
                }

                @Override
                public void onFailure(DataObject object, String reason) {
                    setProgressBar(false);
                    showToast(reason);
                }
            });

        } catch (Exception e) {
            showToast(e.getLocalizedMessage());
        }
    }

    private void saveObject() {
        updateObject();
        setProgressBar(true);
        try {
            final AuthorizedResourceClient client = DataStore.getInstance().getClient(this);
            dataObject.save(client, new DataListener() {
                @Override
                public void onSuccess(DataObject object) {
                    setProgressBar(false);
                    showToast("Object saved successfully");
                }

                @Override
                public void onUnauthorized(DataObject object) {
                    setProgressBar(false);
                    showToast("Authorization error saving object");
                }

                @Override
                public void onFailure(DataObject object, String reason) {
                    setProgressBar(false);
                    showToast(reason);
                }
            });
        } catch (Exception e) {
            showToast(e.getLocalizedMessage());
        }
    }

    private void deleteItem(View v) {
        editorCells.remove(v);
        container.removeView(v);
    }

    private void deleteObjectConfirmation() {
        final AlertDialog dialog = new AlertDialog.Builder(DataEditorActivity.this)
                .setTitle("Delete object with ID '" + dataObject.getObjectId() + "'?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteObject();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void deleteObject() {
        updateObject();
        setProgressBar(true);
        try {
            final AuthorizedResourceClient client = DataStore.getInstance().getClient(this);
            dataObject.delete(client, new DataListener() {
                @Override
                public void onSuccess(DataObject object) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setProgressBar(false);
                            dataObject.clear();
                            populateContainer();
                            showToast("Object deleted successfully");
                        }
                    });
                }

                @Override
                public void onUnauthorized(DataObject object) {
                    setProgressBar(false);
                    showToast("Authorization error deleting object");
                }

                @Override
                public void onFailure(DataObject object, String reason) {
                    setProgressBar(false);
                    showToast(reason);
                }
            });
        } catch (Exception e) {
            showToast(e.getLocalizedMessage());
        }
    }

    private void addItem() {
        if (dataObject != null) {
            final EditorCell cell = getItemCell(1, "", "");
            editorCells.add(0, cell);
            container.addView(cell, 1);
            scrollView.smoothScrollTo(0, 0);
            fixPositions();
            cell.focus();
        }
    }

    private void fixPositions() {
        int position = 1;
        for(final EditorCell cell : editorCells) {
            cell.setPosition(position);
            position += 1;
        }
    }

    private void populateContainer() {
        container.removeAllViews();
        headerCell = getHeaderCell();
        container.addView(headerCell);
        int position = 1;
        editorCells = new ArrayList<EditorCell>();
        for (final Map.Entry<String, Object> entry : dataObject.entrySet()) {
            final EditorCell itemCell = getItemCell(position, entry.getKey(), entry.getValue().toString());
            editorCells.add(itemCell);
            container.addView(itemCell);
            position += 1;
        }
    }

    private EditorCell getHeaderCell() {
        final EditorCell cell = getCell(0, LABEL_CLASS_NAME, LABEL_OBJECT_ID, dataObject.getClassName(), dataObject.getObjectId());
        cell.setHints("May not be blank.", "May not be blank.");
        return cell;
    }

    private EditorCell getItemCell(int position, String value1, String value2) {
        final EditorCell cell = getCell(position, LABEL_KEY, LABEL_VALUE, value1, value2);
        cell.setHints("May not be blank. Must be unique.", "May be blank.");
        cell.setOnLongClickListener(longClickListener);
        return cell;
    }

    private EditorCell getCell(int position, String label1, String label2, String value1, String value2) {
        final EditorCell cell = new EditorCell(this);
        cell.setLabels(label1, label2);
        cell.setPosition(position);
        cell.setValue1(value1);
        cell.setValue2(value2);
        return cell;
    }

    private void updateObject() {
        // Updates the DataObject object to reflect the contents of the data editor cells
        final String className = headerCell.getValue1();
        final String objectId = headerCell.getValue2().toString();
        if (className != null && !className.isEmpty()) {
            dataObject.setClassName(className);
        }
        if (objectId != null && !objectId.isEmpty()) {
            dataObject.setObjectId(objectId);
        }
        dataObject.clear();
        for(final EditorCell cell : editorCells) {
            final String key = cell.getValue1();
            final Object value = cell.getValue2();
            if (key != null && !key.isEmpty() && value != null) {
                dataObject.put(key, value);
            }
        }
    }

    private void deleteModeHint() {
        showToast("Long-touch the left-side of the key/value pair that you want to delete.");
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DataEditorActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setProgressBar(final boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setProgressBarIndeterminateVisibility(b);
            }
        });
    }


}
