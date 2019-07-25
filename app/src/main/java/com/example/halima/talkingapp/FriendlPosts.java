/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.halima.talkingapp;

public class FriendlPosts {
    private String fullname;
    private String message;
    private String username;

    public FriendlPosts() {
    }

    public FriendlPosts(String fullname, String message, String username) {
        this.fullname = fullname;
        this.message = message;
        this.username=username;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String name) {
        this.fullname = name;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }


}
