package com.apivideo.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.apivideo.entity.Users;
import com.apivideo.entity.Videos;
import com.apivideo.service.UsersService;
import com.apivideo.service.VideosService;
import com.apivideo.service.ViewsService;
import com.apivideo.utils.Code;
import com.apivideo.utils.Rest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.apivideo.handler.NonStaticResourceHttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/videos")
@Api(tags = "视频控制器", description = "管理视频相关的API")
public class VideosController {

    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    @Autowired
    private VideosService videosService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ViewsService viewsService;

    public VideosController(NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler) {
        this.nonStaticResourceHttpRequestHandler = nonStaticResourceHttpRequestHandler;
    }

    @ApiOperation(value = "获取所有视频信息", notes = "获取数据库中所有视频的详细信息")
    @GetMapping("/list")
    public List<Videos> getAllVideos() {
        return videosService.list();
    }

    @ApiOperation(value = "按视频ID查找单个视频", notes = "根据视频ID获取视频流")
    @GetMapping("/get/{id}")
    public void getVideoById(HttpServletRequest request, HttpServletResponse response,
                             @ApiParam(value = "视频ID", required = true) @PathVariable Integer id) throws ServletException, IOException {
        Videos video = videosService.getById(id);
        if (video != null) {
            // 记录用户观看历史
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                Users user = usersService.findByUsername(username);
                if (user != null) {
                    viewsService.addViewedVideo(user.getUserId(), id);
                }
            }
            String videoPathUrl = video.getVideoPath();
            Path filePath = Paths.get("src/main/resources/videos/" + videoPathUrl);
            if (Files.exists(filePath)) {
                String mimeType = Files.probeContentType(filePath);
                if (StringUtils.hasText(mimeType)) {
                    response.setContentType(mimeType);
                }
                request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
                nonStaticResourceHttpRequestHandler.handleRequest(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    @ApiOperation(value = "保存上传的视频", notes = "将上传的视频文件保存到服务器")
    @PostMapping(value = "/saveVideo")
    @ResponseBody
    public Map<String, String> saveVideo(@ApiParam(value = "视频文件", required = true) @RequestParam("file") MultipartFile file,
                                         @ApiParam(value = "保存路径", required = true) @RequestParam String SavePath) throws IllegalStateException {
        Map<String, String> resultMap = new HashMap<>();
        try {
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            if (!fileExt.equals("mp4")) {
                resultMap.put("不支持的视频类型", "400");
                return resultMap;
            }
            String videoNameText = file.getOriginalFilename();
            String videoUrl = SavePath + "/" + videoNameText;
            Videos saveVideo = new Videos();
            saveVideo.setDescription(videoNameText);
            saveVideo.setVideoPath(videoUrl);
            videosService.save(saveVideo);

            File filepath = new File(SavePath, file.getOriginalFilename());
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            File fileSave = new File(SavePath, videoNameText);
            file.transferTo(fileSave);

            resultMap.put("newVideoName", videoNameText);
            resultMap.put("resCode", "200");
            resultMap.put("VideoUrl", videoUrl);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resCode", "400");
            return resultMap;
        }
    }

    @ApiOperation(value = "按视频ID删除视频", notes = "根据视频ID删除视频")
    @DeleteMapping("/deleteVideo/{id}")
    public ResponseEntity<Object> deleteVideoById(@ApiParam(value = "视频ID", required = true) @PathVariable Integer id) {
        boolean state = videosService.removeById(id);
        return ResponseEntity.ok(state);
    }

    @ApiOperation(value = "查询当前推荐视频", notes = "获取当前推荐的视频列表")
    @GetMapping("/recommend")
    public List<Videos> getRecommendedVideos() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Users user = usersService.findByUsername(username);
            if (user != null) {
                userId = user.getUserId();
            }
        }
        return videosService.getRecommendedVideos(userId, 4);
    }

    @ApiOperation(value = "点赞视频", notes = "用户可以通过提供视频ID和用户ID点赞视频")
    @PostMapping("/{videoId}/like")
    public String likeVideo(@ApiParam(value = "视频ID", required = true) @PathVariable Integer videoId,
                            @ApiParam(value = "用户ID", required = true) @RequestParam Integer userId) {
        if (videosService.hasLiked(userId, videoId)) {
            videosService.unlikeVideo(userId, videoId);
            return "Video unliked successfully!";
        } else {
            videosService.likeVideo(userId, videoId);
            return "Video liked successfully!";
        }
    }

    @ApiOperation(value = "检查用户是否已点赞视频", notes = "检查用户是否已经点赞某个视频")
    @GetMapping("/{videoId}/hasLiked")
    public boolean hasLiked(@ApiParam(value = "视频ID", required = true) @PathVariable Integer videoId,
                            @ApiParam(value = "用户ID", required = true) @RequestParam Integer userId) {
        return videosService.hasLiked(userId, videoId);
    }

    @ApiOperation(value = "查看用户的所有视频", notes = "根据用户ID获取用户的所有视频")
    @GetMapping("/{userid}")
    public Rest getVideosOfUser(@PathVariable Integer userid, @RequestParam("page") String page) {
        List<Videos> videos = videosService.getVideosOfUser(userid, page);
        return new Rest(Code.rc200.getCode(), videos, "该用户视频列表");
    }

    @ApiOperation(value = "删除用户的视频", notes = "根据用户ID和视频ID删除视频")
    @DeleteMapping("/{userid}")
    public Rest deleteVideo(@ApiParam(value = "用户ID", required = true) @PathVariable Integer deleteUser,
                            @ApiParam(value = "需要删除的视频ID", required = true) @RequestParam("video_id") Integer videoId) {
        if (videosService.deleteVideo(deleteUser, videoId))
            return new Rest(Code.rc200.getCode(), "视频删除成功");
        return new Rest(Code.rc403.getCode(), "不可删除其他用户的视频");
    }

    @ApiOperation(value = "获取视频封面", notes = "根据用户ID和视频ID删除视频")
    @GetMapping("/cover/{videoid}")
    public String getCoverOfVideo(@PathVariable("videoid")String videoid) throws IOException {


        String local_path = "src/main/resources/videos/";

        String cover_path = videosService.getCover(videoid);

        // 读取图片文件
        File file = new File(local_path + cover_path);


        byte[] imageBytes = Files.readAllBytes(file.toPath());

        // 将图片字节数组进行 Base64 编码
        String base64EncodedImage = Base64Utils.encodeToString(imageBytes);

        // 构建响应
//        return new Rest(Code.rc200.getCode(), base64EncodedImage, "该视频的封面");
        return base64EncodedImage;
    }
}
