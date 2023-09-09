package emon.raihan.myapplication

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils


class MainActivity : AppCompatActivity(), CardNfcAsyncTask.CardNfcInterface {
    private lateinit var mNfcAdapter:NfcAdapter
    private lateinit var mCardNfcUtils: CardNfcUtils
    private lateinit var mCardNfcAsyncTask: CardNfcAsyncTask
    private var mIntentFromCreate=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



       mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        //do something if there are nfc module on device

       mCardNfcUtils =  CardNfcUtils(this)
        //next few lines here needed in case you will scan credit card when app is closed
        mIntentFromCreate = true
        onNewIntent(intent)

        checkNfc()
    }

    override fun onPause() {
        super.onPause()
        mCardNfcUtils.disableDispatch()
    }

    override fun onResume() {
        super.onResume()
        mIntentFromCreate = false;
        if (!mNfcAdapter.isEnabled){
            //show some turn on nfc dialog here. take a look in the samle ;-)
        } else {
            mCardNfcUtils.enableDispatch();
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (mNfcAdapter.isEnabled) {
            //this - interface for callbacks
            //intent = intent :)
            //mIntentFromCreate - boolean flag, for understanding if onNewIntent() was called from onCreate or not
            mCardNfcAsyncTask = CardNfcAsyncTask.Builder(this, intent, mIntentFromCreate)
                .build()
        }
    }
    override fun startNfcReadCard() {
        //notify user that scannig start
    }

    override fun cardIsReadyToRead() {
        val card = mCardNfcAsyncTask.cardNumber
        val expiredDate = mCardNfcAsyncTask.cardExpireDate
        val cardType = mCardNfcAsyncTask.cardType

        Log.d("cardNumber", card)
        Log.d("cardExpireDate", expiredDate)
        Log.d("cardType", cardType)
    }

    override fun doNotMoveCardSoFast() {
        //notify user do not move the card
    }

    override fun unknownEmvCard() {
        //notify user that current card has unnown nfc tag
    }

    override fun cardWithLockedNfc() {
        //notify user that current card has locked nfc tag
    }

    override fun finishNfcReadCard() {
        //notify user that scannig finished
    }

    private fun checkNfc() {
        val manager = this.getSystemService(NFC_SERVICE)
        val adapter: NfcAdapter = NfcAdapter.getDefaultAdapter(this)


        if (adapter != null && adapter.isEnabled) {
            Toast.makeText(this, "NFC available", Toast.LENGTH_SHORT).show()
            //Yes NFC available
        } else if (adapter != null && !adapter.isEnabled) {
            Toast.makeText(this, "NFC is not enabled.Need to enable by the user", Toast.LENGTH_SHORT).show()
            //NFC is not enabled.Need to enable by the user.
        } else {
            //NFC is not supported
            Toast.makeText(this, "NFC is not supported", Toast.LENGTH_SHORT).show()
        }

    }
}