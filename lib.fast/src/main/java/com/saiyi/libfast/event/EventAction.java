package com.saiyi.libfast.event;

import android.os.Bundle;

public interface EventAction {

    public int getActionId();

    public void setActionId(int actionId);

    public String getActionName();

    public void setActionName(String actionName);

    public Bundle getActionData();

    public void setActionData(Bundle actionData);
}
