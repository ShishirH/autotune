/*******************************************************************************
 * Copyright (c) 2020, 2021 Red Hat, IBM Corporation and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.autotune.queue;

import com.autotune.em.utils.EMUtils.QueueName;

public class AutotuneQueueFactory {

	public static AutotuneQueue getQueue(String queueName) {
		if(queueName == null) {
			return null;
		}
		if(queueName.equalsIgnoreCase(QueueName.RECMGRQUEUE.name())) {
			return RecMgrQueue.getInstance();
			
		} else if(queueName.equalsIgnoreCase(QueueName.EXPMGRQUEUE.name())) {
			return ExpMgrQueue.getInstance();
		}
		
		return null;
	}
}
