package com.tonikorin.cordova.plugin.autostart;
 
import android.content.Intent;
import android.app.IntentService;//Eu(Daniel) adicionei isto
import android.content.pm.PackageManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import com.tonikorin.cordova.plugin.autostart.AutoStart;
//import android.util.Log;
 
public class AppStarter {

    public static final int BYPASS_USERPRESENT_MODIFICATION = -1;
    
    public void run(Context context, Intent intent, int componentState) {
	     this.run(context, intent, componentState, false);
    }

    public void run(Context context, Intent intent, int componentState, boolean onAutostart) {
        // Enable or Disable UserPresentReceiver (or bypass the modification)
        //Log.d("Cordova AppStarter", "UserPresentReceiver component, new state:" + String.valueOf(componentState));
        if( componentState != BYPASS_USERPRESENT_MODIFICATION ) {
            ComponentName receiver = new ComponentName(context, UserPresentReceiver.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver, componentState, PackageManager.DONT_KILL_APP); 
        }
    
        // Starting your app...
        //Log.d("Cordova AppStarter", "STARTING APP...");
        SharedPreferences sp = context.getSharedPreferences(AutoStart.PREFS, Context.MODE_PRIVATE);
        String packageName = context.getPackageName();
        String className = sp.getString(AutoStart.CLASS_NAME, "");
        if( !className.equals("") ){
            //Log.d("Cordova AppStarter", className);
            Intent serviceIntent = new Intent();
            serviceIntent.setClassName(context, packageName + "." + className);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	          if (onAutostart) {
              serviceIntent.putExtra("cordova_autostart", true);
            }
            //context.startActivity(serviceIntent);//cancei porque esta abrindo o app de maneira não ideal
		
		//--INICIO-------MANEIRA MELHOR DE ABRIR O APP--------
              //String  packageN = "com.grantec.filhorapido";
		 String packageN = context.getPackageName();
                Intent i = context.getPackageManager().getLaunchIntentForPackage(packageN);
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                context.startActivity(i);
		//--FIM-------MANEIRA MELHOR DE ABRIR O APP--------
        }
    }
}
