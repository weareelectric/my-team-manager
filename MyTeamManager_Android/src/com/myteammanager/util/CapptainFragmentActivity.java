/*
 * Copyright 2012 Ubikod
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myteammanager.util;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ubikod.capptain.android.sdk.CapptainAgent;
import com.ubikod.capptain.android.sdk.CapptainAgentUtils;

/**
 * Helper class used to replace Android's android.support.v4.app.FragmentActivity class.
 */
public abstract class CapptainFragmentActivity extends FragmentActivity
{
  private CapptainAgent mCapptainAgent;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    mCapptainAgent = CapptainAgent.getInstance(this);
  }

  @Override
  protected void onResume()
  {
    mCapptainAgent.startActivity(this, getCapptainActivityName(), getCapptainActivityExtra());
    super.onResume();
  }

  @Override
  protected void onPause()
  {
    mCapptainAgent.endActivity();
    super.onPause();
  }

  /**
   * Get the Capptain agent attached to this activity.
   * @return the Capptain agent
   */
  public final CapptainAgent getCapptainAgent()
  {
    return mCapptainAgent;
  }

  /**
   * Override this to specify the name reported by your activity. The default implementation returns
   * the simple name of the class and removes the "Activity" suffix if any (e.g.
   * "com.mycompany.MainActivity" -> "Main").
   * @return the activity name reported by the Capptain service.
   */
  protected String getCapptainActivityName()
  {
    return CapptainAgentUtils.buildCapptainActivityName(getClass());
  }

  /**
   * Override this to attach extra information to your activity. The default implementation attaches
   * no extra information (i.e. return null).
   * @return activity extra information, null or empty if no extra.
   */
  protected Bundle getCapptainActivityExtra()
  {
    return null;
  }
}
