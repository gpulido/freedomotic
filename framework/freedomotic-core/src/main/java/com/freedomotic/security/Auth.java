/**
 *
 * Copyright (c) 2009-2013 Freedomotic team
 * http://freedomotic.com
 *
 * This file is part of Freedomotic
 *
 * This Program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Freedomotic; see the file COPYING.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freedomotic.security;

import com.freedomotic.api.Plugin;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author Matteo Mazzoni <matteo@bestmazzo.it>
 */
public interface Auth {

    /**
     *
     */
    public void initBaseRealm();

    /**
     *
     * @return
     */
    public boolean isInited();

    /**
     *
     * @param key
     * @return
     */
    public boolean isPermitted(String key);

    /**
     *
     * @param subject
     * @param password
     * @return
     */
    public boolean login(String subject, char[] password);

    /**
     *
     * @param subject
     * @param password
     * @return
     */
    public boolean login(String subject, String password);

    /**
     *
     */
    public void logout();

    /**
     *
     * @return
     */
    public Subject getSubject();

    /**
     *
     * @return
     */
    public Object getPrincipal();

    /**
     *
     * @param plugin
     * @param action
     */
    public void pluginExecutePrivileged(Plugin plugin, Runnable action);

    /**
     *
     * @param plugin
     * @param permissions
     */
    public void setPluginPrivileges(Plugin plugin, String permissions);

    /**
     *
     * @return
     */
    public String getPluginDefaultPermission();

    /**
     *
     * @param rm
     */
    public void addRealm(Realm rm);

    /**
     *
     * @param userName
     * @return
     */
    public boolean bindFakeUser(String userName);
}
