package com.gaiamount.module_academy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gaiamount.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-8-3.
 */
public class IntroduceAdapter extends BaseAdapter {
    Context context;
    private ArrayList<String> strings;
    public IntroduceAdapter (Context context,ArrayList<String> strings){
        this.context=context;
        this.strings=strings;
    }
    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.academy_introduce, null);
        CircleImageView imageView= (CircleImageView) convertView.findViewById(R.id.academy_head);
        TextView textView= (TextView) convertView.findViewById(R.id.academy_introduces);
        TextView textViewname= (TextView) convertView.findViewById(R.id.academy_name);
        if(strings.get(position).equals("Mixing Light")){
            imageView.setImageResource(R.mipmap.ic_mx);
            textViewname.setText("Mixing Light");
            textView.setText("在Mixing Light，有大量关于调色、剪辑等后期制作的教学内容，大家可在这里学到如何运用达芬奇和Adobe SpeedGrade CC调色软件进行调色，以" +
                    "及如何运用RED r3d、Apple ProRes、DN×HD等多种编解码器和支持上述软件的工作流程（如1D LUT和3D LUT）。我们还致力于向支持上述调色软件的非线性编辑系统——如Final Cut Pro、Final Cut Pro X、Adobe Premiere Pro、Avid MediaComposer——及相关插件开发商传播专业的调色念。此外，" +
                    "Mixing Light还重视自由调色师和调色公司相关的商业层面的知识和信息传播。Mixing Light博客汇集了业内顶尖的调色师系列访谈节目，颇具特色。");
        }else if(strings.get(position).equals("Patrick Inhofer")){
            imageView.setImageResource(R.mipmap.ic_patrick);
            textViewname.setText("Patrick Inhofer");
            textView.setText("先是为纽约的Final Cut Pro用户群体讲授课程，演示如何在技术和创意层面使用Final Cut Pro的“3-Way调色插件”。后来，他加入该组织的董事会，成为财务总监。在他的导师——该组织的领袖Michael Vitti——过世之后，Patrick接任董事长一职。2010年，他离开该组织，成立“色彩之道”工作室，并专注于调色相关的教学工作。");
        }else if(strings.get(position).equals("Robbie Carman")){
            imageView.setImageResource(R.mipmap.ic_robbie);
            textViewname.setText("Robbie Carman");
            textView.setText("因Lynda网站的培训课程和书籍而为大家所熟知。他还是Final Cut和Premiere的认证培训师（最高级别），并从无到有地建立了相关认证项目，多年来也一直致力于传播该项目。");
        }else if(strings.get(position).equals("Dan Moran")){
            imageView.setImageResource(R.mipmap.ic_robbie);
            textViewname.setText("Dan Moran");
            textView.setText("在爱尔兰的后期工作室工作了几年之后，以达芬奇专家的身份加入到达芬奇团队。之后的几年，他穿梭于世界各地，向许多世界顶尖的调色师讲授如何在日常工作中使用达芬奇软件。此外，他还是达芬奇的演示艺术家，负责研发最吸引眼球的展示方法，展示其最新的功能，吸引新老用户。" +
                    "除了进行教学活动，我们一直不忘自己的调色师身份，始终在从事具体的调色工作。");
        }
        return convertView;
    }

}
