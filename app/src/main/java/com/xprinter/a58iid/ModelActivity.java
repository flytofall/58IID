package com.xprinter.a58iid;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.vondear.rxtools.view.dialog.RxDialogEditSureCancel;
import com.xprinter.a58iid.UTIL.CodeBitmapMaker;
import com.xprinter.a58iid.UTIL.EncodingHandler;
import com.xprinter.a58iid.UTIL.StringUtils;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterPos58;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModelActivity extends AppCompatActivity implements View.OnClickListener{


    @BindView(R.id.container)
    public CoordinatorLayout container;
    @BindView(R.id.times)
    public TextView times;
    @BindView(R.id.checkLable)
    public Button checkLable;
    @BindView(R.id.times_spiner)
    public Spinner spinner_times;
    @BindView(R.id.title)
    public EditText title;
    @BindView(R.id.brand)
    public EditText brand;
    @BindView(R.id.color)
    public EditText color;
    @BindView(R.id.size)
    public EditText size;
    @BindView(R.id.barcode)
    public ImageView barcode;
    @BindView(R.id.HRI)
    public TextView HRI;
    @BindView(R.id.Btprint)
    public Button print;
    private int times1=1;
    private String barcodeHRI;

    private boolean model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        //绑定activity
        ButterKnife.bind( this ) ;
//        print.setFocusable(true);
        title.setCursorVisible(false);

        //model
        model=MainActivity.changeMode;
        initThePrinter();
        setlisenner();

    }

    private void initThePrinter() {
//        if (model){
//            showSnackbar("票据 "+model);
//            checkLable.setVisibility(View.GONE);
//        }else {
//            showSnackbar("标签 "+model);
//            checkLable.setVisibility(View.VISIBLE);
//        }

//        size.setVisibility(View.GONE);
//        HRI.setVisibility(View.GONE);


        MainActivity.binder.writeDataByYouself(new UiExecute() {
            @Override
            public void onsucess() {

            }

            @Override
            public void onfailed() {

            }
        }, new ProcessData() {
            @Override
            public List<byte[]> processDataBeforeSend() {
                List<byte[]> list =new ArrayList<byte[]>();
                if (!model){
                    list.add(DataForSendToPrinterPos58.openOrCloseLableModelInReceip(true));
                    list.add(DataForSendToPrinterPos58.setTheLableWidth(40));

                }else {

                    list.add(DataForSendToPrinterPos58.openOrCloseLableModelInReceip(false));
                }
                return list;
            }
        });
    }

    private void setlisenner() {
//        times.setOnClickListener(this);
        spinner_times.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                times1 = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        barcode.setOnClickListener(this);
        print.setOnClickListener(this);
        checkLable.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();


        if (id == R.id.barcode){
            final RxDialogEditSureCancel rxDialogEditSureCancel = new RxDialogEditSureCancel(this);//提示弹窗
            rxDialogEditSureCancel.getTitleView().setBackgroundResource(R.drawable.barcode_logo2);
            rxDialogEditSureCancel.getEditText().setInputType(EditorInfo.TYPE_CLASS_PHONE);
            rxDialogEditSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rxDialogEditSureCancel.getEditText()!=null||rxDialogEditSureCancel.getEditText().length()!=0){
                        barcodeHRI=rxDialogEditSureCancel.getEditText().getText().toString();
                        HRI.setText(barcodeHRI);
//                        changeBarcode(barcodeHRI);

                    }
                    rxDialogEditSureCancel.cancel();
                }
            });
            rxDialogEditSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxDialogEditSureCancel.cancel();
                }
            });
            rxDialogEditSureCancel.show();

        }
        if (id == R.id.Btprint){

            for(int i =0 ;i<times1;i++){
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        //execute the task
                        print();
                    }
                }, 100);

            }

        }
        //标签下检纸
        if(id ==R.id.checkLable){
            checkPaper();
        }


    }

    /*
    打印
     */
    private void print(){
        final String tileText= title.getText().toString();
        final String brandText= brand.getText().toString();
        final String sizeText = size.getText().toString();
        final String barcodeText = HRI.getText().toString();
        final String colorText = color.getText().toString();
        MainActivity.binder.writeDataByYouself(new UiExecute() {
            @Override
            public void onsucess() {

            }

            @Override
            public void onfailed() {

            }
        }, new ProcessData() {
            @Override
            public List<byte[]> processDataBeforeSend() {
                List<byte[]> list =new ArrayList<byte[]>();
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectCharacterSize(17));
                list.add(DataForSendToPrinterPos58.selectAlignment(1));
                list.add(StringUtils.strTobytes(tileText));
                list.add(DataForSendToPrinterPos58.printAndFeedLine());
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectAlignment(1));
                list.add(StringUtils.strTobytes(brandText));
                list.add(DataForSendToPrinterPos58.printAndFeedLine());
                list.add(StringUtils.strTobytes(sizeText));
                list.add(DataForSendToPrinterPos58.printAndFeedLine());
                list.add(StringUtils.strTobytes(colorText));
                list.add(DataForSendToPrinterPos58.printAndFeedLine());
                list.add(DataForSendToPrinterPos58.selectAlignment(1));
                list.add(DataForSendToPrinterPos58.selectHRICharacterPrintPosition(02));
                list.add(DataForSendToPrinterPos58.setBarcodeWidth(1));
                list.add(DataForSendToPrinterPos58.setBarcodeHeight(50));
                list.add(DataForSendToPrinterPos58.printBarcode(05,barcodeText));
//                list.add(DataForSendToPrinterPos58.printAndFeedLine());
                list.add(DataForSendToPrinterPos58.endOfLable());
                return list;
            }
        });
    }

    private void checkPaper(){
        MainActivity.binder.writeDataByYouself(new UiExecute() {
            @Override
            public void onsucess() {

            }

            @Override
            public void onfailed() {

            }
        }, new ProcessData() {
            @Override
            public List<byte[]> processDataBeforeSend() {
                List<byte[]> list =new ArrayList<byte[]>();
                list.add(DataForSendToPrinterPos58.checkLableAndGap());
                return list;
            }
        });
    }
    /*
    设置barcode图片
     */
    private void changeBarcode(String newHRI){

        if (newHRI.length()!=0){
            HRI.setVisibility(View.GONE);
////            barcode.setBackgroundResource(0);
////            Bitmap bmp=EncodingHandler.creatBarcode(getApplication(),newHRI, BarcodeFormat.CODE_128,650,150,true);
//            Drawable drawable = new BitmapDrawable(bmp);
//            barcode.setImageDrawable(drawable);

        }else {
            Toast.makeText(getApplication(),"null of input",Toast.LENGTH_LONG).show();
        }


    }

    /**
     * 显示提示信息
     * @param showstring 显示的内容字符串
     */
    private void showSnackbar(String showstring){
        Snackbar.make(container, showstring,Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.button_unable)).show();
    }
}
