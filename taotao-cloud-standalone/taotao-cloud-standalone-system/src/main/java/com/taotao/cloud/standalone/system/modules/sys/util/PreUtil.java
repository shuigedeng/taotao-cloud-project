package com.taotao.cloud.standalone.system.modules.sys.util;

import com.taotao.cloud.standalone.system.modules.sys.domain.SysDept;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysMenu;
import com.taotao.cloud.standalone.system.modules.sys.vo.DeptTreeVo;
import com.taotao.cloud.standalone.system.modules.sys.vo.MenuMetaVo;
import com.taotao.cloud.standalone.system.modules.sys.vo.MenuVo;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @Classname PreUtil
 * @Description pre系统用户工具类
 * @Author shuigedeng
 * @since 2019-04-27 16:52
 * @Version 1.0
 */
@UtilityClass
public class PreUtil {


	public List<MenuVo> buildMenus(List<SysMenu> sysMenuList) {
		List<MenuVo> list = new LinkedList<>();

		sysMenuList.forEach(sysMenu -> {
				if (sysMenu != null) {
					List<SysMenu> menuDTOList = sysMenu.getChildren();
					MenuVo menuVo = new MenuVo();
					menuVo.setName(sysMenu.getName());
					menuVo.setPath(sysMenu.getPath());
					// 如果不是外链
					if (sysMenu.getIsFrame()) {
						if (sysMenu.getParentId().equals(0)) {
							//一级目录需要加斜杠，不然访问 会跳转404页面
							menuVo.setPath("/" + sysMenu.getPath());
							menuVo.setComponent(StringUtils.isEmpty(sysMenu.getComponent()) ? "Layout" : sysMenu.getComponent());
						} else if (!StringUtils.isEmpty(sysMenu.getComponent())) {
							menuVo.setComponent(sysMenu.getComponent());
						}
					}
					menuVo.setMeta(new MenuMetaVo(sysMenu.getName(), sysMenu.getIcon()));
					if (menuDTOList != null && menuDTOList.size() != 0 && sysMenu.getType() == 0) {
						menuVo.setChildren(buildMenus(menuDTOList));
						// 处理是一级菜单并且没有子菜单的情况
					} else if (sysMenu.getParentId().equals(0)) {
						menuVo.setAlwaysShow(false);
						menuVo.setRedirect("noredirect");
						MenuVo menuVo1 = new MenuVo();
						menuVo1.setMeta(menuVo.getMeta());
						// 非外链
						if (sysMenu.getIsFrame()) {
							menuVo1.setPath("index");
							menuVo1.setName(menuVo.getName());
							menuVo1.setComponent(menuVo.getComponent());
						} else {
							menuVo1.setPath(sysMenu.getPath());
						}
						menuVo.setName(null);
						menuVo.setMeta(null);
						menuVo.setComponent("Layout");
						List<MenuVo> list1 = new ArrayList<>();
						list1.add(menuVo1);
						menuVo.setChildren(list1);
					}
					list.add(menuVo);
				}
			}
		);
		return list;
	}

	/**
	 * 遍历菜单
	 *
	 * @param menuList
	 * @param menus
	 * @param menuType
	 */
	public void findChildren(List<SysMenu> menuList, List<SysMenu> menus, int menuType) {
		for (SysMenu sysMenu : menuList) {
			List<SysMenu> children = new ArrayList<>();
			for (SysMenu menu : menus) {
				if (menuType == 1 && menu.getType() == 2) {
					// 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
					continue;
				}
				if (sysMenu.getMenuId() != null && sysMenu.getMenuId().equals(menu.getParentId())) {
					menu.setParentName(sysMenu.getName());
					menu.setLevel(sysMenu.getLevel() + 1);
					if (exists(children, menu)) {
						children.add(menu);
					}
				}
			}
			sysMenu.setChildren(children);
			children.sort((o1, o2) -> o1.getSort().compareTo(o2.getSort()));
			findChildren(children, menus, menuType);
		}
	}

	/**
	 * 构建部门tree
	 *
	 * @param sysDepts
	 * @param depts
	 */
	public void findChildren(List<SysDept> sysDepts, List<SysDept> depts) {

		for (SysDept sysDept : sysDepts) {
			List<SysDept> children = new ArrayList<>();
			for (SysDept dept : depts) {
				if (sysDept.getDeptId() != null && sysDept.getDeptId().equals(dept.getParentId())) {
					dept.setParentName(sysDept.getName());
					dept.setLevel(sysDept.getLevel() + 1);
					children.add(dept);
				}
			}
			sysDept.setChildren(children);
			findChildren(children, depts);
		}
	}

	/**
	 * 构建部门tree
	 *
	 * @param sysDepts
	 * @param depts
	 */
	public void findChildren1(List<DeptTreeVo> sysDepts, List<SysDept> depts) {

		for (DeptTreeVo sysDept : sysDepts) {
			sysDept.setId(sysDept.getId());
			sysDept.setLabel(sysDept.getLabel());
			List<DeptTreeVo> children = new ArrayList<>();
			for (SysDept dept : depts) {
				if (sysDept.getId() == dept.getParentId()) {
					DeptTreeVo deptTreeVo1 = new DeptTreeVo();
					deptTreeVo1.setLabel(dept.getName());
					deptTreeVo1.setId(dept.getDeptId());
					children.add(deptTreeVo1);
				}
			}
			sysDept.setChildren(children);
			findChildren1(children, depts);
		}
	}

	/**
	 * 判断菜单是否存在
	 *
	 * @param sysMenus
	 * @param sysMenu
	 * @return
	 */
	public boolean exists(List<SysMenu> sysMenus, SysMenu sysMenu) {
		boolean exist = false;
		for (SysMenu menu : sysMenus) {
			if (menu.getMenuId().equals(sysMenu.getMenuId())) {
				exist = true;
			}
		}
		return !exist;
	}

	/**
	 * 生成BCryptPasswordEncoder密码
	 *
	 * @param rawPass
	 * @return
	 */
	public String encode(String rawPass) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(rawPass);
	}


	/**
	 * 校验密码
	 *
	 * @param newPass
	 * @param passwordEncoderOldPass
	 * @return
	 */
	public boolean validatePass(String newPass, String passwordEncoderOldPass) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(newPass, passwordEncoderOldPass);
	}

	/**
	 * 不重复的验证码
	 *
	 * @param i
	 * @return
	 */
	public String codeGen(int i) {
		char[] codeSequence = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I',
			'O', 'P', 'L', 'K', 'J', 'H', 'G', 'F', 'D',
			'S', 'A', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '0'};
		Random random = new Random();
		StringBuilder stringBuilder = new StringBuilder();
		int count = 0;
		while (true) {
			// 随机产生一个下标，通过下标取出字符数组中对应的字符
			char c = codeSequence[random.nextInt(codeSequence.length)];
			// 假设取出来的字符在动态字符中不存在，代表没有重复的
			if (stringBuilder.indexOf(c + "") == -1) {
				stringBuilder.append(c);
				count++;
				//控制随机生成的个数
				if (count == i) {
					break;
				}
			}
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) throws ParseException {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword("EbfYkitulv73I2p0mXI50JMXoaxZTKJ1");
		System.out.println(encryptor.decrypt("upnvZ1wV5hzuS7Y8UixYJv1tsxNMUHgfnPCIY6Rh3liDh729Ro11+aCDJzI4mILg6eygkozi/NBWXIxhaAE20mS6Bi231/zR+dR66MqfCrJTKFBTLl+MRxt5BPwa6Y0rDzyeP1YOaCLc7FmZsq5uH+mkbH/xiOfbeeZhVjGBwNXhVTFqxgtb0wbgzvh996PCzMDLsT36nA+J6xtW+zgZZb3vshSCoHv3BrwiludhiV8="));

	}


}
