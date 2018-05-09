package com.example.peggytsai.restaurantreservationapp.Menu.modifymenu;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetImageTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MenuModifyFragmentUpdate extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private List<Menu> menus_list;
    private Button bt_cancel;
    private Button bt_delete;
    private Button bt_save;   //git test
    private Button bt_img;
    private EditText et_name;
    private EditText et_price;
    private ImageView image_view;


    private TextView tt_toolbar;
    private TextView btMenuModifyItem;

    private MyTask MenuUpdataTask;
    private  MyTask MenuDeleteTask;
    private Menu menu;

    private byte[] image=null;

    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_IMAGE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri, croppedImageUri;
    private final static String TAG = "MenuUpdate";

    private int isdown=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_menu_modify_item, container, false);

        findbutton(view);

        tt_toolbar.setText("修改菜單");
        btMenuModifyItem.setText("");




        return view;

    }



    private void findbutton(View view) {

        String url = Common.URL + "/MenuServlet";
        image_view = view.findViewById(R.id.image_view);
        int imageSize = getResources().getDisplayMetrics().widthPixels / 4;


        Bundle bundle = getArguments();

        if (bundle != null) {
             menu = (Menu) bundle.getSerializable("MENU");

            if (menu != null) {

                et_name = view.findViewById(R.id.et_name);
                et_price = view.findViewById(R.id.et_price);

                et_name.setHint(menu.getName());
                et_price.setHint(menu.getPrice());

                new MenuGetImageTask(image_view).execute(url, menu.getId(), imageSize);
            }
        }//get bundle

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        btMenuModifyItem = view.findViewById(R.id.btMenuModifyItem);

        bt_cancel = view.findViewById(R.id.bt_cancel);
        bt_delete = view.findViewById(R.id.bt_delete);
        bt_save = view.findViewById(R.id.bt_save);
        bt_img = view.findViewById(R.id.bt_img);
        bt_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                if (Common.networkConnected(getActivity())) {//檢查網路連線

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "menuUpdata");
                    jsonObject.addProperty("menu_id", new Gson().toJson( menu.getId()  ));

                    if (image == null) {
                        Common.showToast(getActivity(), " no image ");
                        return false;
                    }

                    String imageBase64 = "";
                    imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
                    jsonObject.addProperty("imageBase64", imageBase64);


                    MenuUpdataTask = new MyTask(Common.URL+"/MenuServlet", jsonObject.toString());
                    MenuUpdataTask.execute();
                } else {
                    Common.showToast(getContext(), "text_NoNetwork");
                }
                getFragmentManager().popBackStack();
                return true;
            }
        });


        bt_cancel.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_img.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_img:

//                isdown=1;

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
                File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");
                contentUri = FileProvider.getUriForFile(
                        getActivity(), getActivity().getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if (Common.isIntentAvailable(getActivity(), intent)) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                } else {
                    Common.showToast(getActivity(), "NoCameraApp");
                }


            break;
            case R.id.bt_delete:

                if (Common.networkConnected(getActivity())) {//檢查網路連線

                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("action", "menuDelete");
                    jsonObject.addProperty("menu_id", menu.getId());

                    MenuDeleteTask = new MyTask(Common.URL + "/MenuServlet", jsonObject.toString());
                    MenuDeleteTask.execute();


                    getFragmentManager().popBackStack();

                }

                break;
            case R.id.bt_save:
                //選取圖片



                    if (et_name.getText().toString().trim().isEmpty()) {
                        et_name.setError("請輸入正確格式");
                        return;
                    }

                    if (et_price.getText().toString().trim().isEmpty()) {
                        et_price.setError("請輸入正確格式");
                        return;
                    }



                    if (Common.networkConnected(getActivity())) {//檢查網路連線
                        menu.setName(et_name.getText().toString().trim());
                        menu.setPrice(et_price.getText().toString().trim());

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "menuUpdata");
                        jsonObject.addProperty("menu", new Gson().toJson( menu  ));
                        String imageBase64 = "";
                        if(image==null){
                            //純文字 不需要menu_id image
                        }else{
                            imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
                            jsonObject.addProperty("imageBase64", imageBase64);

                        }


                        MenuUpdataTask = new MyTask(Common.URL+"/MenuServlet", jsonObject.toString());
                        MenuUpdataTask.execute();
                    } else {
                        Common.showToast(getContext(), "text_NoNetwork");
                    }

                    getFragmentManager().popBackStack();


                break;
            case R.id.bt_cancel:
                getFragmentManager().popBackStack();
                break;


            default:
                break;


        }






    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;
                case REQ_PICK_IMAGE:
                    Uri uri = intent.getData();
                    crop(uri);
                    break;
                case REQ_CROP_PICTURE:
                    Log.d(TAG, "REQ_CROP_PICTURE: " + croppedImageUri.toString());
                    try {
                        Bitmap picture = BitmapFactory.decodeStream(
                                getActivity().getContentResolver().openInputStream(croppedImageUri));
                        image_view.setImageBitmap(picture);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        image = out.toByteArray();
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        croppedImageUri = Uri.fromFile(file);
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // the recipient of this Intent can read soruceImageUri's data
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // set image source Uri and type
            cropIntent.setDataAndType(sourceImageUri, "image/*");
            // send crop message
            cropIntent.putExtra("crop", "true");
            // aspect ratio of the cropped area, 0 means user define
            cropIntent.putExtra("aspectX", 0); // this sets the max width
            cropIntent.putExtra("aspectY", 0); // this sets the max height
            // output with and height, 0 keeps original size
            cropIntent.putExtra("outputX", 0);
            cropIntent.putExtra("outputY", 0);
            // whether keep original aspect ratio
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            // whether return data by the intent
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQ_CROP_PICTURE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Common.showToast(getActivity(), "This device doesn't support the crop action!");
        }
    }


}
