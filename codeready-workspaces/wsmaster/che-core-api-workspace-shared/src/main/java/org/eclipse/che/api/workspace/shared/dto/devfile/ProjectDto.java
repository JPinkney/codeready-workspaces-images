/*
 * Copyright (c) 2012-2021 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.api.workspace.shared.dto.devfile;

import org.eclipse.che.api.core.model.workspace.devfile.Project;
import org.eclipse.che.dto.shared.DTO;

/** @author Sergii Leshchenko */
@DTO
public interface ProjectDto extends Project {

  @Override
  String getName();

  void setName(String name);

  ProjectDto withName(String name);

  @Override
  SourceDto getSource();

  void setSource(SourceDto source);

  ProjectDto withSource(SourceDto source);

  @Override
  String getClonePath();

  void setClonePath(String clonePath);

  ProjectDto withClonePath(String clonePath);
}
