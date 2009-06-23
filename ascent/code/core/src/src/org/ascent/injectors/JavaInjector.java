 /**************************************************************************
 * Copyright 2008 Jules White                                              *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/

package org.ascent.injectors;

import org.ascent.injectors.annotations.ForEachAnnotation;
import org.ascent.injectors.annotations.InsertAnnotation;
import org.ascent.injectors.annotations.ReplaceAnnotation;


public class JavaInjector extends CommentBasedInjector {

	public static JavaInjector createDefaultInjector() {
		JavaInjector injector = new JavaInjector();
		injector.addAnnotationHandler(new ForEachAnnotation());
		injector.addAnnotationHandler(new InsertAnnotation());
		injector.addAnnotationHandler(new ReplaceAnnotation());
		return injector;
	}

	public static final String MULTI_LINE_COMMENT_START = "/**";
	public static final String MULTI_LINE_COMMENT_END = "**/";

	public JavaInjector() {
		super(MULTI_LINE_COMMENT_START, MULTI_LINE_COMMENT_END);
	}

}
