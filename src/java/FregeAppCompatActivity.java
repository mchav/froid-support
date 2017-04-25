package froid.support.v7.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import frege.run7.Func;
import frege.run7.Lazy;
import frege.run7.Thunk;
import frege.run.Kind;
import frege.run.RunTM;
import frege.runtime.Meta;
import frege.runtime.Phantom.RealWorld;
import frege.prelude.PreludeBase;

import java.lang.reflect.Method;

public class FregeAppCompatActivity extends AppCompatActivity {
    
    Func.U<RealWorld, Short> onPauseLambda = null;
    Func.U<RealWorld, Short> onResumeLambda = null;
    Func.U<Bundle, Func.U<RealWorld, Short>> onSavedInstanceStateLambda = null;
    Func.U<Integer, Func.U<Integer, Func.U<PreludeBase.TMaybe<Intent>, Func.U<RealWorld, Short>>>> onActivityResultLambda = null;
    Func.U<MenuItem, Func.U<RealWorld, Boolean>> onOptionsItemSelectedLambda = null;
    Func.U<Menu, Func.U<RealWorld, Boolean>> onCreateOptionsMenuLambda = null;

    // executes io action given as parameter
    public void setOnPause(Func.U<RealWorld, Short> lambda) {
        this.onPauseLambda = lambda;
    }

    public void setOnResume(Func.U<RealWorld, Short> lambda) {
        this.onResumeLambda = lambda;
    }

    public void setOnSavedInstanceState(Func.U<Bundle, Func.U<RealWorld, Short>> lambda) {
        this.onSavedInstanceStateLambda = lambda;
    }

    public void setOnActivityResult(Func.U<Integer, Func.U<Integer, Func.U<PreludeBase.TMaybe<Intent>, Func.U<RealWorld, Short>>>> onActivityResultLambda) {
        this.onActivityResultLambda = onActivityResultLambda;
    }

    public void setOnCreateOptionsMenu(Func.U<Menu, Func.U<RealWorld, Boolean>> onCreateOptionsMenuLambda) {
        this.onCreateOptionsMenuLambda = onCreateOptionsMenuLambda;
    }

    public void setOnOptionsItemSelected(Func.U<MenuItem, Func.U<RealWorld, Boolean>> onOptionsItemSelectedLambda) {
        this.onOptionsItemSelectedLambda = onOptionsItemSelectedLambda;
    }

    // reflection methods
    private Object invokeStaticActivityMethod(String methodName, Object[] args, String signature) {
        Method fregeMethod = null;
        try {
            fregeMethod = this.getClass().getDeclaredMethod(methodName,
                FregeAppCompatActivity.class,
                Lazy.class);
        } catch (NoSuchMethodException nsm) {
            android.util.Log.e("FROID SYSTEM",
                "Method " + methodName +
                " is not defined. Make sure your onCreate Function is defined as " +
                signature, nsm);
            this.finishAffinity();
        }

        Object invokedMethod = null;

        try {
            invokedMethod = fregeMethod.invoke(null, args);
        } catch (Exception e) { // none of the invocation exceptions should happen
            android.util.Log.e("FROID SYSTEM", "Failed to call " + methodName, e);
            this.finishAffinity();
        }
        return invokedMethod;
    }

    private Object run(Object invokedMethod) {
        if (invokedMethod == null) return null;
        
        final Func.U<Object,Short> res = RunTM.<Func.U<Object,Short>>cast(
                invokedMethod).call();
        return PreludeBase.TST.run(res).call();
    }

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreludeBase.TMaybe<Bundle> sis = savedInstanceState == null ?
         PreludeBase.TMaybe.DNothing.<Bundle>mk(): 
         PreludeBase.TMaybe.DJust.<Bundle>mk(Thunk.<Bundle>lazy(savedInstanceState));
        Object invokedOnCreate = invokeStaticActivityMethod("onCreate", new Object[] {this, sis}, "onCreate :: MutableIO AppCompatActivity -> IO ()");
        run(invokedOnCreate);   
    }

    @Override
    public void onPause() {
        super.onPause();
        run(onPauseLambda);
    }

    @Override
    public void onResume() {
        super.onResume();
        run(onResumeLambda);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (onSavedInstanceStateLambda != null)
            run(onSavedInstanceStateLambda.apply(Thunk.<Bundle>lazy(savedInstanceState)).call());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (onActivityResultLambda == null) return;
        
        // apply the arguments incrementally
        Func.U<Integer, Func.U<PreludeBase.TMaybe<Intent>, Func.U<RealWorld, Short>>> first = 
            RunTM.<Func.U<Integer, Func.U<PreludeBase.TMaybe<Intent>, Func.U<RealWorld, Short>>>>cast(
                onActivityResultLambda.apply(Thunk.<Integer>lazy(requestCode)).call());
        Func.U<PreludeBase.TMaybe<Intent>, Func.U<RealWorld, Short>> second = 
            RunTM.<Func.U<PreludeBase.TMaybe<Intent>, Func.U<RealWorld, Short>>>cast(
                first.apply(Thunk.<Integer>lazy(resultCode)).call());

        // wrap intent in maybe
        PreludeBase.TMaybe<Intent> d = data == null ?
         PreludeBase.TMaybe.DNothing.<Intent>mk(): 
         PreludeBase.TMaybe.DJust.<Intent>mk(Thunk.<Intent>lazy(data));

        run(second.apply(Thunk.<PreludeBase.TMaybe<Intent>>lazy(d)).call());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (onCreateOptionsMenuLambda == null)
            return true;
        
        return ((Boolean) run(onCreateOptionsMenuLambda.apply(Thunk.<Menu>lazy(menu)).call())).booleanValue();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (onOptionsItemSelectedLambda == null)
            return super.onOptionsItemSelected(item);
        
        return ((Boolean) run(onOptionsItemSelectedLambda.apply(Thunk.<MenuItem>lazy(item)).call())).booleanValue();
    }
}
