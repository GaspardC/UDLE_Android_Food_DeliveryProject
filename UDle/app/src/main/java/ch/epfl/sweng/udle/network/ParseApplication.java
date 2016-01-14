/*
 *  Copyright (c) 2014, Parse, LLC. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Parse.
 *
 *  As with any software that integrates with the Parse platform, your use of
 *  this software is subject to the Parse Terms of Service
 *  [https://www.parse.com/about/terms]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package ch.epfl.sweng.udle.network;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;


public class ParseApplication extends Application {


  @Override
  public void onCreate() {
    super.onCreate();
/*    // Required - Initialize the Parse SDK
    Parse.enableLocalDatastore(this);

    Parse.initialize(this, "8Sl49UiakBdglrkaeJNIG4bF74qdApgMR6fS9VRe", "7ID0uhsKi7Syix6joJkXi8R5gfh42cOUWUDRQnSq");

    Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

    ParseFacebookUtils.initialize(this);

    // Optional - If you don't want to allow Twitter login, you can
    // remove this line (and other related ParseTwitterUtils calls)*/

      //Initialize the parse Order server object
      ParseObject.registerSubclass(ParseUserOrderInformations.class);
      ParseObject.registerSubclass(ParseRestaurantMark.class);
      Parse.initialize(this, "v22C8nu3xMqTVZUq9yeuOLD6xbTXqWpCM1XuyQ7U", "9SgG0yX869nVvDExV3PdTy9HVEwQzdLwjtVloHTp");


      ParseFacebookUtils.initialize(getApplicationContext());

  }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
