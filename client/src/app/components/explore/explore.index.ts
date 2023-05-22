import { NewCommentComponent } from "./comments/new-comment/new-comment.component";
import { CommentsComponent } from "./comments/view-comments/comments.component";
import { ExploreProjectsComponent } from "./explore-projects/explore-projects.component";
import { ProjectMainComponent } from "./project/project-main/project-main.component";
import { ProjectHeaderComponent } from "./project/projectHeader/project-header.component";
import { ViewAnnouncementComponent } from "./viewAnnouncements/view-announcement.component";
import { RequestComponent } from "./viewRequest/request/request.component";

export const exploreComponents = [
    ExploreProjectsComponent,
    RequestComponent,
    ProjectMainComponent,
    ProjectHeaderComponent,
    ViewAnnouncementComponent,
    CommentsComponent,
    NewCommentComponent,
]